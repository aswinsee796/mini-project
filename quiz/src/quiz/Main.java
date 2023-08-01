package quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Quiz quiz = new Quiz();
        quiz.begin();

    }
}

class Quiz
{
    void begin()
    
    {
    	   String url = "jdbc:mysql://localhost:3306/quiz_application";
           String user = "root";
           String dbPassword = "TIGER";
    	System.out.println(" \t \t <------Welcome to online Quiz Application!!------> \n \n");
        Question[] questions = new Question[5];

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT * FROM quiz_questions ORDER BY RAND() LIMIT 5";
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    int index = 0;
                    while (resultSet.next()) {
                        String question = resultSet.getString("question");
                        String option1 = resultSet.getString("option1");
                        String option2 = resultSet.getString("option2");
                        String option3 = resultSet.getString("option3");
                        String option4 = resultSet.getString("option4");
                        String answer = resultSet.getString("answer");

                        questions[index++] = new Question(question, option1, option2, option3, option4, new Answer(answer));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int countTotal = 0;
        int countRight = 0;
        int countWrong = 0;

        for(Question q: questions)
        {
            System.out.println(q.getQuestion());
            System.out.println("A : " +q.getOption1());
            System.out.println("B : " +q.getOption2());
            System.out.println("C : " +q.getOption3());
            System.out.println("D : " +q.getOption4());

            String answer = "";

            char ans;
            System.out.println("Enter your answer");
            Scanner scan = new Scanner(System.in);
            ans = scan.next().charAt(0);

            switch(ans)
            {
                case 'A':
                    answer = q.getOption1();
                    break;
                case 'B':
                    answer = q.getOption2();
                    break;
                case 'C':
                    answer = q.getOption3();
                    break;
                case 'D':
                    answer = q.getOption4();
                    break;
                default:break;
            }
            System.out.println("Your answer " + answer );
            if(answer.equals(q.getAnswer().getAnswer()))
            {
                System.out.println("<------------------------------------------------------>");
                System.out.println("                  Correct Answer                      ");
                System.out.println("<------------------------------------------------------>");
                countRight++;
            }
            else
            {
                System.out.println("<------------------------------------------------------>");
                System.out.println("                  Wrong Answer                      ");
                System.out.println("<------------------------------------------------------>");
                countWrong++;
            }
            System.out.println("============================================================================================");
            countTotal++;
        }

        Result result = new Result(countTotal,countRight,countWrong);
        result.showResult();
    }
}

class Question
{

    String question;
    String option1;
    String option2;
    String option3;
    String option4;
    Answer answer;

    public Question(String question, String option1, String option2, String option3, String option4, Answer answer) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public Answer getAnswer() {
        return answer;
    }
}

class Answer
{
    String answer;

    public Answer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}

interface IResult
{
    void showResult();
    double showPercentage(int correctAnswers,int totalQuestions);
    String showPerformance(double percentage);
}

class Result implements IResult
{
    int totalQuestions;
    int correctAnswers;
    int wrongAnswers;

    public Result(int totalQuestions, int correctAnswers, int wrongAnswers) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }

    @Override
    public void showResult() {
        System.out.println("Your results!");
        System.out.println("Total Questions " + totalQuestions);
        System.out.println("Number of correct answers " + correctAnswers);
        System.out.println("Number of wrong answers " + wrongAnswers);
        System.out.println("Percentage " + showPercentage(correctAnswers,totalQuestions));
        System.out.println("Your performance " + showPerformance(showPercentage(correctAnswers,totalQuestions)));
        saveResultToDatabase();

    }

    @Override
    public double showPercentage(int correctAnswers, int totalQuestions) {
        //System.out.println(correctAnswers + " " + totalQuestions);
        return (double) (correctAnswers / (double)totalQuestions) * 100 ;
    }
    private void saveResultToDatabase() {
        String url = "jdbc:mysql://localhost:3306/quiz_application";
        String user = "root";
        String dbPassword = "TIGER";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String insertQuery = "INSERT INTO quiz_results (total_questions, correct_answers, wrong_answers, percentage, performance) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, totalQuestions);
                preparedStatement.setInt(2, correctAnswers);
                preparedStatement.setInt(3, wrongAnswers);
                preparedStatement.setDouble(4, showPercentage(correctAnswers, totalQuestions));
                preparedStatement.setString(5, showPerformance(showPercentage(correctAnswers, totalQuestions)));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String showPerformance(double percentage) {
        String performance = "";

        if(percentage >= 60)
        {
            performance = "Good";
        }
        else if(percentage <= 40)
        {
            performance = "Poor";
        }

        return performance;

    }
}