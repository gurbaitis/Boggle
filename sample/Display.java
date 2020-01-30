package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashSet;

//***********************************
// Gabriel Urbaitis
//
// The Display class is used for displaying
// the GUI for the game and handling actions
// made by the player, such as pressing the
// start button, or selecting the board size
// in the main menu or selecting letters
// and pressing the enter button in the
// game menu.
//***********************************
public class Display extends Application implements EventHandler<Event>
{
  static int size = 4;
  private final Integer startTime = 180;
  private VBox vbox = new VBox(10);
  private Button startButton;
  private Button enterButton;
  private Text validation = new Text("");
  private Text validWordsList = new Text("Valid Words:");
  private Text invalidWords = new Text("Invalid Words:");
  private HashSet<String> playedCorrect = new HashSet<String>();
  private HashSet<String> playedIncorrect = new HashSet<String>();
  private Text sizeLabel = new Text("Board Size:");
  private ComboBox<String> combo = new ComboBox<>();
  private GridPane gridpane = new GridPane();
  private GridPane cGridPane = new GridPane();
  private Canvas canvas = new Canvas(45 * size + 5, 45 * size + 5);
  private Board tray = new Board(size);
  private int oldX = 0;
  private int oldY = 0;
  private boolean firstMouse = true;
  private String toCheck;
  private int points = 0;
  private Timeline time = new Timeline();
  private  KeyFrame frame;
  private Stage primaryStage;
  private Integer seconds = startTime;
  private Label label = new Label();

  //***********************************
  //Input: Stage primaryStage: the stage
  //for building the window
  //Returns void
  //Instantiates the basic framework
  //required to lay the main menu
  //and board GUI.
  //***********************************
  @Override
  public void start(Stage primaryStage) throws Exception
  {
    points = 0;
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(10, 10, 10, 10));
    this.primaryStage = primaryStage;
    combo.getItems().addAll("4", "5", "6", "7", "8", "9", "10");
    combo.setValue("4");
    invalidWords.setFill(Color.RED);
    mainMenu();
    this.primaryStage.setTitle("Welcome to Boggle!");
    this.primaryStage.setScene(new Scene(vbox, 465, 610));
    this.primaryStage.show();
  }
  //***********************************
  //Input: Event event: the event to
  //be handled, either mouse or action
  //Returns void
  //Determines whether the event is
  //a mouse event or action event and
  //casts it to the appropriate type
  //and calls the associated handling
  //method
  //***********************************
  @Override
  public void handle(Event event)
  {
    //handle event testing
    Object source = event.getSource();
    if (source instanceof MouseEvent)
    {
      handleMouseEvent((MouseEvent) source);
    }
    if (source instanceof ActionEvent)
    {
      handleActionEvent((ActionEvent) source);
    }
  }
  //***********************************
  //Input: None
  //Returns void
  //Runs the GUI for playing a game of
  //boggle. Creates a board for playing,
  //Displays text relating to the
  //correctness of each move, draws
  //outlines around selected letters
  //and computes their validity when
  //the enter button is pressed
  //***********************************
  private void playGame() throws Exception
  {
    points = 0;
    tray = new Board(Display.size);
    canvas = new Canvas(45 * size + 5, 45 * size + 5);
    this.primaryStage.setTitle("Boggle!");
    vbox.setPadding(new Insets(10, 10, 10, 10));

    enterButton = new Button("Check Word");
    enterButton.setOnAction(this::handleActionEvent);

    gridpane.setOnMouseClicked(this::handleMouseEvent);
    cGridPane.add(canvas, 0, 0);

    label.setTextFill(Color.BLACK);
    label.setFont(Font.font(20));

    displayBoard();
    gridpane.setHgap(5);
    gridpane.setVgap(5);
    gridpane.setPadding(new Insets(5, 5, 5, 5));
    StackPane layer = new StackPane();
    layer.getChildren().addAll(cGridPane, gridpane);
    layer.setTranslateX(230 - (size * 22.5));

    vbox.getChildren().addAll(validation, layer, enterButton, validWordsList, invalidWords, label);
    count();
    canvas.getGraphicsContext2D().setFill(Color.RED);
  }
  //***********************************
  //Input: None
  //Returns void
  //Sets the timer and the initial score,
  //adds a combo box for selecting the
  //size of the board, as well as a label
  //and a start button to launch the
  //game GUI.
  //***********************************
  private void mainMenu() throws Exception
  {
    points = 0;
    seconds = startTime;

    combo.valueProperty().addListener(new ChangeListener<String>()
    {
      @Override
      public void changed(ObservableValue ov, String old, String newS)
      {
        Display.size = Integer.valueOf(newS);
        tray = new Board(Display.size);
        canvas = new Canvas(45 * size + 5, 45 * size + 5);
      }
    });
    startButton = new Button("Start Game");
    startButton.setOnAction(this::handleActionEvent);
    vbox.getChildren().addAll(startButton, sizeLabel, combo);
  }
  //***********************************
  //Input: None
  //Returns void
  //Runs the timer countdown using a
  //Timeline iterating a frame each
  //second.
  //***********************************
  private void count()
  {
    time.setCycleCount(Timeline.INDEFINITE);
    if (time != null)
    {
      time.stop();
    }
    frame = new KeyFrame(Duration.seconds(1), this::handleActionEvent);
    time.getKeyFrames().add(frame);
    time.playFromStart();
  }
  //***********************************
  //Input: Action Event ae: either
  //the start button, enter button
  //or frame representing a second.
  //Returns void
  //The start button sets all of the
  //required fields for a new game
  //and calls playGame. The enter
  //button assesses the input made
  //by the player, updates the score
  //and labels appropriately and clears
  //the selecting graphics. The frame
  //updates the displayed timer according
  //to the time left and launches the
  //game over screen when time is up.
  //***********************************
  private void handleActionEvent(ActionEvent ae)
  {
    //handle event
    Object source = ae.getSource();
    if (source == startButton)
    {
      try
      {
        vbox.getChildren().clear();
        cGridPane.getChildren().clear();
        gridpane.getChildren().clear();
        oldX = 0;
        oldY = 0;
        firstMouse = true;
        validWordsList.setText("Valid Words:");
        invalidWords.setFill(Color.RED);
        invalidWords.setText("Invalid Words:");
        playedCorrect.clear();
        playedIncorrect.clear();
        playGame();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    if (source == enterButton)
    {
      if (tray.getDictionary().getSet().contains(toCheck))
      {
        if (tray.getValidWords().contains(toCheck))
        {
          if (playedCorrect.contains(toCheck))
          {
            validation.setFill(Color.RED);
            setValidation(toCheck + " has already been played. " + "Total Points: " + points);
          } else
          {
            points += (toCheck.length() - 2);
            validation.setFill(Color.BLACK);
            setValidation(toCheck + " is a valid word! " + "Total Points: " + points);
            validWordsList.setText(validWordsList.getText() + " " + toCheck);
            playedCorrect.add(toCheck);
          }
        } else
        {
          if (playedIncorrect.contains(toCheck))
          {
            validation.setFill(Color.RED);
            setValidation(toCheck + " has already been attempted. " + "Total Points: " + points);
          } else
          {
            validation.setFill(Color.RED);
            setValidation(toCheck + " is not on the board " + "Total Points: " + points);
            invalidWords.setText(invalidWords.getText() + " " + toCheck);
            playedIncorrect.add(toCheck);
          }
        }
      } else
      {
        if (playedIncorrect.contains(toCheck))
        {
          validation.setFill(Color.RED);
          setValidation(toCheck + " has already been attempted. " + "Total Points: " + points);
        } else
        {
          validation.setFill(Color.RED);
          setValidation(toCheck + " is not in the dictionary. " + "Total Points: " + points);
          invalidWords.setText(invalidWords.getText() + " " + toCheck);
          playedIncorrect.add(toCheck);
        }
      }
      toCheck = null;
      firstMouse = true;
      canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    } else if (source == frame)
    {
      seconds--;
      Integer currentSec;
      if (seconds >= 119)
      {
        currentSec = seconds - 120;
        if (currentSec <= 9)
        {
          label.setText("Time Left: 2:0" + currentSec.toString());
        } else
        {
          label.setText("Time Left: 2:" + currentSec.toString());
        }
      }
      if (seconds >= 60 && seconds <= 119)
      {
        currentSec = seconds - 60;
        if (currentSec <= 9)
        {
          label.setText("Time Left: 1:0" + currentSec.toString());
        } else
        {
          label.setText("Time Left: 1:" + currentSec.toString());
        }
      } else if (seconds < 60)
      {
        if (seconds <= 9)
        {
          label.setText("Time Left: 0:0" + seconds.toString());
        } else
        {
          label.setText("Time Left: 0:" + seconds.toString());
        }
      }
      if (seconds <= 0)
      {
        time.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("        Game Over!          Score: " + points);
        alert.show();
        setValidation("");
        vbox.getChildren().clear();
        try
        {
          mainMenu();
        } catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }
  //***********************************
  //Input: MouseEvent me: a click on
  //the board.
  //Returns void
  //Draws an outline on the clicked
  //square and updates the word to be
  //checked with its letter, provided
  //it is adjacent to any previously
  //clicked square.
  //***********************************
  private void handleMouseEvent(MouseEvent me)
  {
    //handle event
    int newX = (int) (me.getX() - 5) / 45;
    int newY = (int) (me.getY() - 5) / 45;
    int differenceX = newX - oldX;
    int differenceY = newY - oldY;
    if (firstMouse)
    {
      canvas.getGraphicsContext2D().fillRect(newX * 45 + 2, newY * 45 + 2, 46, 46);
      toCheck = tray.getBoard()[newX][newY].toString();
      oldX = newX;
      oldY = newY;
      firstMouse = false;
    } else if (!firstMouse && !((differenceX == 0) && (differenceY == 0))
            && (differenceX >= -1) && (differenceY >= -1) && (differenceX <= 1)
            && (differenceY <= 1))
    {
      canvas.getGraphicsContext2D().fillRect(newX * 45 + 2, newY * 45 + 2, 46, 46);
      toCheck = toCheck + tray.getBoard()[newX][newY];
      oldX = newX;
      oldY = newY;
    }
  }
  //***********************************
  //Input: None
  //Returns void
  //Runs through the character array
  //representing the board, and adds
  //the corresponding character image
  //to the screen.
  //***********************************
  private void displayBoard()
  {
    for (int i = 0; i < size; i++)
    {
      for (int j = 0; j < size; j++)
      {
        gridpane.add(new ImageView(new Image("sample/" + tray.getBoard()[i][j] + ".png",
                40, 40, false, false)), i, j);
      }
    }
  }
  //***********************************
  //Input: String message: The message
  //indicating the validity of the
  //players last move.
  //Returns void
  //Sets the text in the validation
  //field on the screen.
  //***********************************
  private void setValidation(String message)
  {
    validation.setText(message);
  }

  //***********************************
  // No Input
  // Returns void
  // launches the arguments necessary for
  // running a JavaFx application.
  //***********************************
  public static void main(String[] args)
  {
    launch(args);
  }
}
