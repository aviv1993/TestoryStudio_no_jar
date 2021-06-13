define_event("GotoSiteHome", function (e) {
    if (CTX.getEntityById("CurrentPages")[e.s] != "Main Menu") {
        embedEvent("GotoMainMenu", { s: e.s });
    }

    click(e.s, "//*[@data-key='home']");
    page(e.s, "Site Home");
})

define_event("GotoMainMenu", function (e) {
    click(e.s, "//span[contains(@class,'site-name d-none')]");
    page(e.s, "Main Menu");
})

define_event("GotoCourse", function (e) {
    if (CTX.getEntityById("CurrentPages")[e.s] != "Site Home") {
        embedEvent("GotoSiteHome", { s: e.s });
    }
    click(e.s, "//*[text()[normalize-space()='" + e.course + "']]");
    page(e.s, "Course:" + e.course);
})


define_event("GotoQuiz", function (e) {
    if (CTX.getEntityById("CurrentPages")[e.s] != "Course:" + e.course) {
        embedEvent("GotoCourse", { s: e.s, course: e.course });
    }
    click(e.s, "//*[text()='" + e.quiz + "']")
    page(e.s, "quiz:" + e.course + "/" + e.quiz);
})



define_event("TurnSiteEditingOn", function (e) {
    if (CTX.getEntityById("CurrentPages")[e.s] != "Site Home") {
        embedEvent("GotoSiteHome", { s: e.s });
    }

    click(e.s, "//a[@id='action-menu-toggle-2']/i");
    click(e.s, "//a[contains(@href, 'edit=on')]");
})


define_event("TurnCourseEditingOn", function (e) {
    if (CTX.getEntityById("CurrentPages")[e.s] != "Course:" + e.course) {
        embedEvent("GotoCourse", { s: e.s, course: e.course });
    }
    click(e.s, "//button[@type='submit']");
})


define_event("AddCourse", function (e) {
    if (CTX.getEntityById("CurrentPages")[e.s] != "Site Home") {
        embedEvent("GotoSiteHome", { s: e.s });
    }

    click(e.s, "//*[contains(@action,'edit.php')]//button");

    page(e.s, "Course:" + e.course);

    writeText(e.s, "//label[@for='id_fullname']/following::input[@id='id_fullname']", e.fullname);
    writeText(e.s, "//label[@for='id_shortname']/following::input[@id='id_shortname']", e.shortname);

    if (e.category) selectByVisibleText(e.s, "//label[@for='id_category']/following::select[@id='id_category']", e.category);
    if (e.visibility) selectByVisibleText(e.s, "//label[@for='id_visible']/following::select[@id='id_visible']", e.visibility);

    if (e.start) with (e.start) {
        if (day) selectByVisibleText(e.s, "//p[@id='id_startdate_label']/following::select[@name='startdate[day]']", day);
        if (month) selectByVisibleText(e.s, "//p[@id='id_startdate_label']/following::select[@name='startdate[month]']", month);
        if (year) selectByVisibleText(e.s, "//p[@id='id_startdate_label']/following::select[@name='startdate[year]']", year);
        if (hour) selectByVisibleText(e.s, "//p[@id='id_startdate_label']/following::select[@name='startdate[hour]']", hour);
        if (minute) selectByVisibleText(e.s, "//p[@id='id_startdate_label']/following::select[@name='startdate[minute]']", minute);
    }

    if (e.end) with (e.end) {
        if (day) selectByVisibleText(e.s, "//p[@id='id_enddate_label']/following::select[@name='enddate[day]']", day);
        if (month) selectByVisibleText(e.s, "//p[@id='id_enddate_label']/following::select[@name='enddate[month]']", month);
        if (year) selectByVisibleText(e.s, "//p[@id='id_enddate_label']/following::select[@name='enddate[year]']", year);
        if (hour) selectByVisibleText(e.s, "//p[@id='id_enddate_label']/following::select[@name='enddate[hour]']", hour);
        if (minute) selectByVisibleText(e.s, "//p[@id='id_enddate_label']/following::select[@name='enddate[minute]']", minute);
    }

    if (e.id) writeText(e.s, "//label[@for='id_idnumber']/following::input[@id='id_idnumber']", e.id);
    if (e.summary) writeText(e.s, "//label[@for='id_summary_editor']/following::div[@id='id_summary_editoreditable']", e.summary);

    if (e.format) with (e.format) {
        click(e.s, "//*[@aria-controls='id_courseformathdr']");
        if (format) selectByVisibleText(e.s, "//label[@for='id_format']/following::select[@id='id_format']", format);
        if (number_of_sections) selectByVisibleText(e.s, "//label[@for='id_numsections']/following::select[@id='id_numsections']", number_of_sections);
        if (hidden_sections) selectByVisibleText(e.s, "//label[@for='id_hiddensections']/following::select[@id='id_hiddensections']", hidden_sections);
        if (layout) selectByVisibleText(e.s, "//label[@for='id_coursedisplay']/following::select[@id='id_coursedisplay']", layout);
    }

    if (e.appearance) with (e.appearance) {
        click(e.s, "//*[@aria-controls='id_appearancehdr']");
        if (language) selectByVisibleText(e.s, "//label[@for='id_lang']/following::select[@id='id_lang']", language);
        if (number_of_announcements) selectByVisibleText(e.s, "//label[@for='id_newsitems']/following::select[@id='id_newsitems']", number_of_announcements);
        if (show_gradebook) selectByVisibleText(e.s, "//label[@for='id_showgrades']/following::select[@id='id_showgrades']", show_gradebook);
        if (show_activity) selectByVisibleText(e.s, "//label[@for='id_showreports']/following::select[@id='id_showreports']", show_activity);
    }
    if (e.files) with (e.files) {
        click(e.s, "//*[@aria-controls='id_filehdr']");
        if (maxupload) selectByVisibleText(e.s, "//label[@for='id_maxbytes']/following::select[@id='id_maxbytes']", maxupload);
    }
    if (e.completion) with (e.completion) {
        click(e.s, "//*[@aria-controls='id_completionhdr']");
        if (tracking) selectByVisibleText(e.s, "//label[@for='id_enablecompletion']/following::select[@id='id_enablecompletion']", tracking);
    }
    if (e.groups) with (e.groups) {
        click(e.s, "//*[@aria-controls='id_groups']");

        if (mode) selectByVisibleText(e.s, "//label[@for='id_groupmode']/following::select[@id='id_groupmode']", mode);
        if (force) selectByVisibleText(e.s, "//label[@for='id_groupmodeforce']/following::select[@id='id_groupmodeforce']", force);
        if (defaultid) selectByVisibleText(e.s, "//label[@for='id_defaultgroupingid']/following::select[@id='id_defaultgroupingid']", defaultid);
    }

    if (e.role_renaming) with (e.role_renaming) {
        click(e.s, "//*[@aria-controls='id_rolerenaming']");
        if (role_1) writeText(e.s, "//label[@for='id_role_1']/following::input[@id='id_role_1']", role_1);
        if (role_2) writeText(e.s, "//label[@for='id_role_2']/following::input[@id='id_role_2']", role_2);
        if (role_3) writeText(e.s, "//label[@for='id_role_3']/following::input[@id='id_role_3']", role_3);
        if (role_4) writeText(e.s, "//label[@for='id_role_4']/following::input[@id='id_role_4']", role_4);
        if (role_5) writeText(e.s, "//label[@for='id_role_5']/following::input[@id='id_role_5']", role_5);
        if (role_6) writeText(e.s, "//label[@for='id_role_6']/following::input[@id='id_role_6']", role_6);
        if (role_7) writeText(e.s, "//label[@for='id_role_7']/following::input[@id='id_role_7']", role_7);
        if (role_8) writeText(e.s, "//label[@for='id_role_8']/following::input[@id='id_role_8']", role_8);
    }

    click(e.s, "//*[@name='saveandreturn']");
    page(e.s, "Site Home");
})


define_event("EnrolUserToCourse", function (e) {
    if (CTX.getEntityById("CurrentPages")[e.s] != "Course:" + e.course) {
        embedEvent("GotoCourse", { s: e.s, course: e.course });
    }
    click(e.s, "//span[text()='Participants']");
    click(e.s, "//input[@value='Enrol users']");
    click(e.s, "//a[@class='moreless-toggler']")
    selectByValue(e.s, "//select[@id='id_startdate']", "4");
    click(e.s, "//span[text()='▼']");
    click(e.s, "(//span[text()='" + e.user + "'])[2]");
    if (e.role) selectByVisibleText(e.s, "//label[@for='id_roletoassign']/following::select[@id='id_roletoassign']", e.role);
    click(e.s, "//button[@data-action='save']");
})

define_event("AddH5P", function (e) {
    if (CTX.getEntityById("CurrentPages")[e.s] != "Course:" + e.course) {
        embedEvent("GotoCourse", { s: e.s, course: e.course });
    }
    click(e.s, "//li[@id='section-1']//*[@class='section-modchooser-text']");
    click(e.s, "//*[@id='all-4']//div[1]//div[@data-internal='h5pactivity']//img");
    writeText(e.s, "//input[@name='name']", e.name);
    writeText(e.s, "//*[@id='id_introeditoreditable']", e.description);


    if (e.allowDownload || e.embedButton || e.copyrightButton) {
        click(e.s, "//a[normalize-space()='H5P options']");
    }
    
    if (e.allowDownload) {
        click(e.s, "//input[@id='id_displayopt_export']");
    }

    if (e.embedButton) {
        click(e.s, "//input[@name='displayopt[embed]']");
    }

    if (e.copyrightButton) {
        click(e.s, "//input[@name='displayopt[copyright]']");
    }

    click(e.s, '//*[@class="dndupload-arrow"]');
    click(e.s, '//*[@class="fp-repo-name"]');
    click(e.s, '//*[contains(@src, "repository_recent")]');
    click(e.s, '//*[.="'+e.activity+'"]');
    click(e.s, '//*[contains(@class, "fp-select-confirm")]');
    click(e.s, "//input[@name='submitbutton']");
      
    waitForVisibility(e.s, "//*[contains(.,'This content is displayed in preview mode')]");
    
    switchFrame(e.s, '//iframe[contains(@class,"h5p-player")]');
    switchFrame(e.s, '//iframe[contains(@class,"h5p-iframe")]');

    if (e.allowDownload)
        waitForVisibility(e.s, "//*[contains(text(),'Reuse')]");
    else
        waitForInvisibility(e.s, "//*[contains(text(),'Reuse')]");

    // if (e.embedButton)
    //     waitForVisibility(e.s, "//*[contains(.,'Embed')]");
    // else
    //     waitForInvisibility(e.s, "//*[contains(.,'Embed')]");

    // if (e.copyrightButton)
    //     waitForVisibility(e.s, "//*[contains(.,'Rights of use')]");
    // else
    //     waitForVisibility(e.s, "//*[contains(.,'Rights of use')]");

    switchFrame(e.s, 'Main Frame');
})


define_event("AttemptMultipleChoice", function (e) {
    click(e.s, "//*[@data-key='home']");
    click(e.s, "//*[text()='" + e.course + "']");
    click(e.s, "//*[contains(text(),'" + e.name + "')]");
    
    waitForInvisibility(e.s, "//*[contains(.,'This content is displayed in preview mode')]");
    
    switchFrame(e.s, '//iframe[contains(@class,"h5p-player")]');
    switchFrame(e.s, '//iframe[contains(@class,"h5p-iframe")]');
    
    click(e.s, "//*[contains(text(),'Correct one')]");
    click(e.s, "//*[contains(text(),'Check')]");

    switchFrame(e.s, 'Main Frame');


});

