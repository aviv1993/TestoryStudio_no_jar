var quizname;

testSuite('Simple H5P test')


bthread('Add a h5pactivity activity to a course', function () {

  startSession('T1', 'http://localhost/login/index.php')

  login
    ({ s: 'T1', username: 'teacher1', password: 'teacherPASSWORD1!' })

  turnCourseEditingOn
    ({ s: 'T1', course: 'MyCourse' })

  addH5P
    ({
      s: 'T1', course: 'MyCourse', section: '1', name: 'my_h5p_' + random.string(4), description: 'description',
      copyrightButton: true,
      embedButton: false,
      activity: 'multiple-choice-2-6.h5p' //'ipsums.h5p'
    })

  endSession('T1');
})


bthread('Attemp h5pactivity', function () {
  when(Any('AddH5P'), function (e) {
    if (e.activity == 'multiple-choice-2-6.h5p') {
      startSession('S1', 'http://localhost/login/index.php');

      login
        ({ s: 'S1', username: 'student1', password: 'studentPASSWORD1!' })

      attemptMultipleChoice
        ({ s: 'S1', course: e.course, name: e.name })

      endSession('S1');
    }
  })
})


