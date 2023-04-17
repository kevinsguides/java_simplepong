import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

//This class must extend JPanel so we can use paintComponent and implement MouseMotionListener to track mouse
public class PongGame extends JPanel implements MouseMotionListener {

    // Constants for window width and height, in case we want to change the
    // width/height later
    static final int WINDOW_WIDTH = 640, WINDOW_HEIGHT = 480;
    private Ball gameBall, futureBall;
    private Paddle userPaddle, pcPaddle;
    private int userScore, pcScore;

    private int userMouseY; // to track the user's mouse position
    private int bounceCount; // to count number of ball bounces between paddles

    private int detectedCollideY; // the location we think the ball will collide with the PC's paddle
    private boolean pcGotToTarget; // whether or not the PC has reached the target paddle location
    private int oscillationDirection; // the direction and amount the PC paddle is oscillating up/down
    private boolean pcAccidentalMiss; // whether or not the PC "accidentally" misses the next ball

    /**
     * Standard constructor for a PongGame
     */
    public PongGame() {

        // Make the ball and paddles
        gameBall = new Ball(300, 200, 3, 3, 3, Color.YELLOW, 10);
        // futureBall is a clone of gameBall, but with a white color
        futureBall = new Ball(gameBall);
        userPaddle = new Paddle(10, 200, 75, 3, Color.BLUE);
        pcPaddle = new Paddle(610, 200, 75, 3, Color.RED);

        // Set instance variables to zero to start
        userMouseY = 0;
        userScore = 0;
        pcScore = 0;
        bounceCount = 0;

        // set detectedCollideY to -1
        detectedCollideY = -1;
        pcGotToTarget = false;
        oscillationDirection = 5;
        pcAccidentalMiss = false;

        // listen for motion events on this object
        addMouseMotionListener(this);

    }

    /**
     * resets the game to start a new round
     */
    public void reset() {

        // reset gameBall and paddles
        gameBall = new Ball(320, 220, 3, 3, 3, Color.YELLOW, 10);
        userPaddle = new Paddle(10, 200, 75, 3, Color.BLUE);
        pcPaddle = new Paddle(610, 200, 75, 3, Color.RED);
        bounceCount = 0;
        pcAccidentalMiss = false;

        // reset futureBall
        futureBall = new Ball(gameBall);
        detectedCollideY = -1;
        pcGotToTarget = false;

    }

    /**
     * Updates and draws all the graphics on the screen
     */
    public void paintComponent(Graphics g) {

        // draw the background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // draw the ball
        gameBall.paint(g);

        // draw the paddles
        userPaddle.paint(g);
        pcPaddle.paint(g);

        // update score
        g.setColor(Color.WHITE);
        // the drawString method needs a String to print, and a location to print it at.
        g.drawString("Score - User [ " + userScore + " ]   PC [ " + pcScore + " ]", 250, 20);

    }

    /**
     * Called once each frame to handle essential game operations
     */
    public void gameLogic() {

        // advance gameBall one frame
        gameBall.handleAllMovement(0, WINDOW_HEIGHT);

        // if we haven't yet detected the collision point
        if (detectedCollideY == -1) {
            // loop ten steps ahead
            for (int i = 0; i < 10; i++) {

                futureBall.handleAllMovement(0, WINDOW_HEIGHT, userPaddle);

                // if prediction ball goes past the pc paddle x, we know this is where the
                // paddle needs to move to
                if (futureBall.getX() > pcPaddle.getX()) {
                    detectedCollideY = futureBall.getY();

                    // if the PC is set to accidentally miss, increase detectedCollideY by 100
                    if (pcAccidentalMiss) {
                        detectedCollideY += 75;
                    }
                    // stop the loop here
                    break;
                }
            }
        }

        movePaddles();

        checkPaddleBounces();

        checkLosses();

    }

    private void movePaddles(){
        // if the pcPaddle is within 3 pixels of the detected collision point, stop
        // moving
        if (pcPaddle.isNear(detectedCollideY, 3) && !pcGotToTarget) {
            pcGotToTarget = true;
        }

        if (!pcGotToTarget) {
            // move the pc paddle towards the detected collision point
            pcPaddle.moveTowards(detectedCollideY);
        } else {
            pcPaddle.oscillate(10, 10, detectedCollideY);
        }

        // move the paddle towards where the mouse is
        userPaddle.moveTowards(userMouseY);
    }

    private void checkPaddleBounces(){
        // check if ball collides with either paddle
        if (userPaddle.checkCollision(gameBall)) {
            gameBall.reverseX();
            // nudge the ball one pixel to the right of the user paddle
            gameBall.setX(userPaddle.getX() + Paddle.PADDLE_WIDTH + 1);
            bounceCount++;
        }
        // if we collide with pcPaddle
        if (pcPaddle.checkCollision(gameBall)) {
            gameBall.reverseX();
            // nudge prediction ball one pixel to the left of the pc paddle
            gameBall.setX(pcPaddle.getX() - 10);
            // we need to reset the prediction ball
            futureBall = new Ball(gameBall);
            // reset the detected collision point
            detectedCollideY = -1;
            // track if we made it to the target position
            pcGotToTarget = false;
            bounceCount++;
            // there is a 1/3 chance of setting pcAccidentalMiss to true
            if ((int) (Math.random() * 3) == 0) {
                pcAccidentalMiss = true;
            }
        }

        // increase speed after certain amount of bounces
        if (bounceCount == 3) {
            // reset counter
            bounceCount = 0;
            // increase ball speed
            gameBall.increaseSpeed();
        }
    }

    private void checkLosses(){
        // check if someone lost
        if (gameBall.getX() < 0) {
            // player has lost
            pcScore++;
            reset();
        } else if (gameBall.getX() > WINDOW_WIDTH) {
            // pc has lost
            userScore++;
            reset();
        }
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update saved mouse position on mouse move
        userMouseY = e.getY();

    }
}