/*
SIMPLE PONG With better organization

This code goes with the third section of the tutorial series.
It's a continuation of the previous two sections.
Basically just modifies the code from last section to be better compartmentalized
with proper division of methods. Namely, the gameLogic method is broken up into smaller
chunks.

https://kevinsguides.com

NOTICE TO STUDENTS
If you use this project to help with academic work,
I'd encourage you to write it from scratch by following
the guide or video and making your own modifications.
If you fail to do this, it may be flagged by your instructor as plagiarism!!!

MIT License

Copyright (c) 2022 Kevin's Guides (Kevin Olson)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    //declare and initialize the frame
    static JFrame f = new JFrame("Pong");

    public static void main(String[] args) {

        //make it so program exits on close button click
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //the size of the game will be 640x480, the size of the JFrame needs to be slightly larger
        f.setSize(655,510);

        //create a new PongGame and add it to the JFrame
        PongGame game = new PongGame();
        f.add(game);

        //show the frame/program window
        f.setVisible(true);

        //make a new swing Timer
        Timer timer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //game logic
                game.gameLogic();

                //repaint the screen
                game.repaint();


            }
        });

        //start the timer after it's been created
        timer.start();

    }
}