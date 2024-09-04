import java.util.*;

public class OnlineExamSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        ExamService examService = new ExamService();
        ExamTakingService examTakingService = new ExamTakingService();

        User currentUser = null;
        boolean running = true;

        System.out.println("Welcome to the Online Examination System!");

        while (running) {
            if (currentUser == null) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Login");
                System.out.println("2. Register (Admin only)");
                System.out.println("3. Exit");
            } else {
                if (currentUser.getUserType() == UserType.STUDENT) {
                    System.out.println("\nSelect an option:");
                    System.out.println("1. View Available Exams");
                    System.out.println("2. Take Exam");
                    System.out.println("3. View Exam Results");
                    System.out.println("4. Logout");
                } else if (currentUser.getUserType() == UserType.ADMIN) {
                    System.out.println("\nSelect an option:");
                    System.out.println("1. Create Exam");
                    System.out.println("2. Add Question to Exam");
                    System.out.println("3. View All Exams");
                    System.out.println("4. Logout");
                }
            }

            if (currentUser == null) {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        currentUser = userService.login(username, password);
                        if (currentUser == null) {
                            System.out.println("Invalid username or password.");
                        }
                        break;
                    case 2:
                        if (currentUser != null && currentUser.getUserType() == UserType.ADMIN) {
                            System.out.print("Enter new username: ");
                            String newUsername = scanner.nextLine();
                            System.out.print("Enter password: ");
                            String newPassword = scanner.nextLine();
                            userService.registerAdmin(newUsername, newPassword);
                            System.out.println("Admin registered successfully.");
                        } else {
                            System.out.println("Only admins can register.");
                        }
                        break;
                    case 3:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } else {
                if (currentUser.getUserType() == UserType.STUDENT) {
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    switch (choice) {
                        case 1:
                            List<Exam> availableExams = examService.getExams();
                            if (availableExams.isEmpty()) {
                                System.out.println("No exams available.");
                            } else {
                                System.out.println("Available Exams:");
                                for (Exam exam : availableExams) {
                                    System.out.println("- " + exam.getExamName());
                                }
                            }
                            break;
                        case 2:
                            availableExams = examService.getExams();
                            if (availableExams.isEmpty()) {
                                System.out.println("No exams available.");
                            } else {
                                System.out.println("Choose an exam to take:");
                                for (int i = 0; i < availableExams.size(); i++) {
                                    System.out.println((i + 1) + ". " + availableExams.get(i).getExamName());
                                }
                                System.out.print("Enter exam number: ");
                                int examNumber = scanner.nextInt();
                                scanner.nextLine(); // Consume newline character

                                if (examNumber >= 1 && examNumber <= availableExams.size()) {
                                    Exam selectedExam = availableExams.get(examNumber - 1);
                                    examTakingService.takeExam(selectedExam, currentUser);
                                } else {
                                    System.out.println("Invalid exam number.");
                                }
                            }
                            break;
                        case 3:
                            List<Exam> examsTaken = examTakingService.getExamsTaken();
                            if (examsTaken.isEmpty()) {
                                System.out.println("No exams taken yet.");
                            } else {
                                System.out.println("Exams Taken:");
                                for (Exam exam : examsTaken) {
                                    System.out.println("- " + exam.getExamName());
                                }
                            }
                            break;
                        case 4:
                            currentUser = null;
                            System.out.println("Logged out.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                    }
                } else if (currentUser.getUserType() == UserType.ADMIN) {
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    switch (choice) {
                        case 1:
                            System.out.print("Enter exam name: ");
                            String examName = scanner.nextLine();
                            examService.createExam(examName);
                            break;
                        case 2:
                            List<Exam> allExams = examService.getExams();
                            if (allExams.isEmpty()) {
                                System.out.println("No exams available to add questions.");
                            } else {
                                System.out.println("Choose an exam to add a question:");
                                for (int i = 0; i < allExams.size(); i++) {
                                    System.out.println((i + 1) + ". " + allExams.get(i).getExamName());
                                }
                                System.out.print("Enter exam number: ");
                                int examNumber = scanner.nextInt();
                                scanner.nextLine(); // Consume newline character

                                if (examNumber >= 1 && examNumber <= allExams.size()) {
                                    Exam selectedExam = allExams.get(examNumber - 1);
                                    System.out.print("Enter question text: ");
                                    String questionText = scanner.nextLine();
                                    System.out.print("Enter number of options: ");
                                    int numOptions = scanner.nextInt();
                                    scanner.nextLine(); // Consume newline character

                                    String[] options = new String[numOptions];
                                    for (int i = 0; i < numOptions; i++) {
                                        System.out.print("Enter option " + (i + 1) + ": ");
                                        options[i] = scanner.nextLine();
                                    }

                                    System.out.print("Enter correct option index (1-" + numOptions + "): ");
                                    int correctOptionIndex = scanner.nextInt() - 1;
                                    scanner.nextLine(); // Consume newline character

                                    Question question = new Question(questionText, options, correctOptionIndex);
                                    examService.addQuestionToExam(selectedExam.getExamName(), question);
                                } else {
                                    System.out.println("Invalid exam number.");
                                }
                            }
                            break;
                        case 3:
                            List<Exam> exams = examService.getExams();
                            if (exams.isEmpty()) {
                                System.out.println("No exams available.");
                            } else {
                                System.out.println("All Exams:");
                                for (Exam exam : exams) {
                                    System.out.println("- " + exam.getExamName());
                                    List<Question> questions = exam.getQuestions();
                                    for (Question question : questions) {
                                        System.out.println("  " + question.getQuestionText());
                                        String[] options = question.getOptions();
                                        for (int i = 0; i < options.length; i++) {
                                            System.out.println("    " + (i + 1) + ". " + options[i]);
                                        }
                                        System.out.println("  Correct Option: " + (question.getCorrectOptionIndex() + 1));
                                    }
                                    System.out.println();
                                }
                            }
                            break;
                        case 4:
                            currentUser = null;
                            System.out.println("Logged out.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                    }
                }
            }
        }

        scanner.close();
    }
}

class User {
    private String username;
    private String password;
    private UserType userType;

    public User(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }
}

enum UserType {
    STUDENT,
    ADMIN
}

class Question {
    private String questionText;
    private String[] options;
    private int correctOptionIndex;

    public Question(String questionText, String[] options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

class Exam {
    private String examName;
    private List<Question> questions;

    public Exam(String examName) {
        this.examName = examName;
        this.questions = new ArrayList<>();
    }

    public String getExamName() {
        return examName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }
}

class UserService {
    private List<User> users;

    public UserService() {
        this.users = new ArrayList<>();
        // Initial admin user
        users.add(new User("admin", "admin123", UserType.ADMIN));
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void registerAdmin(String username, String password) {
        users.add(new User(username, password, UserType.ADMIN));
    }
}

class ExamService {
    private List<Exam> exams;

    public ExamService() {
        this.exams = new ArrayList<>();
    }

    public void createExam(String examName) {
        exams.add(new Exam(examName));
        System.out.println("Exam '" + examName + "' created successfully.");
    }

    public void addQuestionToExam(String examName, Question question) {
        for (Exam exam : exams) {
            if (exam.getExamName().equals(examName)) {
                exam.addQuestion(question);
                System.out.println("Question added to exam '" + examName + "'.");
                return;
            }
        }
        System.out.println("Exam '" + examName + "' not found.");
    }

    public List<Exam> getExams() {
        return exams;
    }
}

class ExamTakingService {
    private List<Exam> examsTaken;

    public ExamTakingService() {
        this.examsTaken = new ArrayList<>();
    }

    public void takeExam(Exam exam, User student) {
        Scanner scanner = new Scanner(System.in);
        int score = 0;

        System.out.println("Taking exam: " + exam.getExamName());
        System.out.println("Answer the following questions (choose the correct option):");

        for (Question question : exam.getQuestions()) {
            System.out.println("\n" + question.getQuestionText());
            String[] options = question.getOptions();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print("Your answer (1-" + options.length + "): ");
            int userAnswer = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (userAnswer == question.getCorrectOptionIndex() + 1) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect.");
            }
        }

        System.out.println("Exam finished. Your score: " + score + "/" + exam.getQuestions().size());
        examsTaken.add(exam);
    }

    public List<Exam> getExamsTaken() {
        return examsTaken;
    }
}

