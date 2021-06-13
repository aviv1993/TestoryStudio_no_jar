define_event("Login", function (e) {
    writeText(e.s, "//input[@name='username']", e.username);
    writeText(e.s, "//input[@name='password']", e.password);
    click(e.s, "//*[@id='loginbtn']");
});

define_event("TurnEditingOn", function (e) {
    
    click(e.s, "//*[@data-key='home']");
    click(e.s, "//*[text()='" + e.course + "']");
    click(e.s, "//a[@id='action-menu-toggle-2']/i");
    click(e.s, "//button[@type='submit']");
})

define_event("AddQuiz", function (e) {
    click(e.s, "//*[@data-key='home']");
    click(e.s, "//*[text()='" + e.course + "']");
    click(e.s, "//li[@id='section-1']//*[@class='section-modchooser-text']");
    click(e.s, "//*[@id='all-4']//div[1]//div[@data-internal='quiz']//img");
    writeText(e.s, "//input[@name='name']", e.quizname);
    click(e.s, "//*[@name='submitbutton2']");
})

define_event("AddTrueFalseQuestionToQuiz", function (e) {
    click(e.s, "//*[text()='" + e.quiz + "']")
    click(e.s, '//*[@id="action-menu-toggle-3"]/i')
    click(e.s, "//a[contains(@href,'http://localhost/mod/quiz/edit.php')]")
    click(e.s, "//a[@id='action-menu-toggle-3']//span[1]")
    click(e.s, "//a[@data-action='addquestion']//span[1]")
    click(e.s, "//label[@for='item_qtype_truefalse']")
    click(e.s, "//div[@class='submitbuttons']//input[1]")
    writeText(e.s, "//input[@name='name']", e.questionname)
    writeText(e.s, "//*[@id='id_questiontexteditable']", e.question_text)
    writeText(e.s, "//*[@id='id_defaultmark']", "1")
    //selectByVisibleText(e.s, "//*[@name='correctanswer']", e.answer)
    selectByValue(e.s, "//*[@name='correctanswer']", e.answer)
    click(e.s, "//*[@id='id_submitbutton']")
    verifyText(e.s, "//span[@class='questionname' and .='" + e.questionname + "']", e.questionname)
})

define_event("TrueFalseQuestionAttempt", function (e) {
    click(e.s, "//*[@data-key='home']");
    click(e.s, "//*[text()='" + e.course + "']");
    click(e.s, "//*[contains(text(),'" + e.quiz + "')]");
    click(e.s, "//button[@type='submit']");
    click(e.s, "//*[text()='" + e.question_text + "']/following::div//*[@value='" + e.answer + "']");
    click(e.s, "//*[@name='next']");
    click(e.s, '//*[@action="http://localhost/mod/quiz/processattempt.php"]//button');
    click(e.s, "//input[contains(@id,'id_yuiconfirmyes')]");
})