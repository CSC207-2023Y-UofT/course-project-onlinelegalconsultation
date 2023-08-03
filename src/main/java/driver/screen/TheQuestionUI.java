package driver.screen;

import adapter.controller.ControlContainer;
import businessrule.usecase.PostDisplayFormatter;
import entity.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TheQuestionUI extends JPanel implements ActionListener {
    ControlContainer controlContainer;
    CardLayout cardLayout;
    JPanel screens;
    int userId;
    String userName;
    String title;
    String type;
    LocalDate deadline;
    Map<Integer, PostDisplayFormatter> postMap;

    public TheQuestionUI(ControlContainer controlContainer, CardLayout cardLayout,
                         JPanel screens, int userId, String userName,  String title,
                         String type, LocalDate deadline, Map<Integer, PostDisplayFormatter> postMap) {
        this.controlContainer = controlContainer;
        this.cardLayout = cardLayout;
        this.screens = screens;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.type = type;
        this.deadline = deadline;
        this.postMap = postMap;
        //UserName and UserId
        String helloMessageString = "Hello, " + userName + "(" + userId + ")";
        JLabel helloMessage = new JLabel(helloMessageString);
        //Question title and type
        String titleLineString = "(" + type + ")" + title;
        JLabel titleLine = new JLabel(titleLineString);
        titleLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Question deadline
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String deadlineString = "Deadline: " + deadline.format(formatter);
        JLabel deadlineLine = new JLabel(deadlineString);
        //The scrollable posts
        int numberOfPosts = postMap.size();
        JScrollPane postScrollPane = new JScrollPane();
        postScrollPane.setLayout(new GridLayout(numberOfPosts, 1));
        for (PostDisplayFormatter post : postMap.values()){
            //read all variables from displayFormatter
            String name = post.getName();
            String postText = post.getPostText();
            String userType;
            if (post.isClient()){
                userType = "Client";
            } else{
                userType = "Attorney";
            }
            String postDate = post.getCreateAt().format(formatter);
            //lines
            String idLine = name + "(" + userType + ")";
            String dateLine = "Posted on: " + postDate;
            //Format them all into a button
            JTextArea postArea = new JTextArea();
            postArea.setEditable(false);
            String overallText = idLine + "\n" + dateLine + "\n" + postText;
            postArea.setText(overallText)
            postArea.setPreferredSize(new Dimension(300, 30));
            //Add the postArea into the scrollpanel
            postScrollPane.add(postArea);
        }
        //The three buttons
        JPanel buttons = new JPanel();
        JButton postReply = new JButton("Post reply");
        JButton closeQuestion = new JButton("Close question");
        JButton rateQuestion = new JButton("Rate question");

        buttons.add(postReply);
        buttons.add(closeQuestion);
        buttons.add(rateQuestion);
        postReply.addActionListener(this);
        closeQuestion.addActionListener(this);
        rateQuestion.addActionListener(this);

        //Add everythin into JPanel
        this.add(helloMessage);
        this.add(titleLine);
        this.add(deadlineLine);
        this.add(postScrollPane);
        this.add(buttons);


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
