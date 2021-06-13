var quizname;


testSuite('Add quizzes and questions')

bthread('A teacher adds quizzes with questions', function () {

  startSession('T1', 'http://localhost/login/index.php');


  login
    ({ s: 'T1', username: 'teacher1', password: 'teacherPASSWORD1!' });

  turnEditingOn
    ({ s: 'T1', course: 'MyCourse' });

  for (var i = 0; i < 3; i++) {
    quizname = (random.string(4));

    addQuiz
      ({ s: 'T1', course: 'MyCourse', quizname: quizname });

    addTrueFalseQuestionToQuiz
      ({ s: 'T1', course: 'MyCourse', quiz: quizname, questionname: 'A question', question_text: 'Are you here?', answer: '1' });
  }

  endSession('T1');
})

bthread('A student answers questions', function () {
  
  startSession('S1', 'http://localhost/login/index.php');
  
  login
  ({ s: 'S1', username: 'student1', password: 'studentPASSWORD1!' });
  
  when(Any("AddTrueFalseQuestionToQuiz"), function (e) {
    trueFalseQuestionAttempt
    ({ s: 'S1', course: e.course, quiz: e.quiz, question_text: e.question_text, answer: '1' });
    
    trueFalseQuestionAttempt
    ({ s: 'S1', course: e.course, quiz: e.quiz, question_text: e.question_text, answer: '0' });
  })

  endSession('S1');
})

