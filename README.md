This app`s goal is to control table tennis ball launcher used for table tennis training. The launcher is mounted on top of the table, on the opposite side of the user. It consists of 2 wheels spinning in opposite directions. When a ball is pushed between them they accelerate it and shoot it out of the barrel. The whole shooting mechanism is mounted on top of a servo which can rotate it allowing to shoot the ball at different angles. Controller on the launchers side is an Arduino Nano - it controls speed of the motors and angle of the servo. It connects to the app via Bluetooth.

After launching the app the main screen is shown. It allows user to switch between different activities and displays the information about a connection status.

<img src="https://github.com/mmajchrzak557/ball-launcher/blob/master/res/main.jpg" align="center" width="100" height="300">

The "Połącz" activity is used to establish a Bluetooth connection to the launcher controller. It consists of a text field and a button. After navigating to this screen the field is automatically filled with a default MAC address, but user can change it. Then, after pressing the button and if the address is correct, the app tries to establish a connection to the launcher controller and returns an information about the state of the connection. The connection state is also shown in the main screen.

![connect](https://github.com/mmajchrzak557/ball-launcher/blob/master/res/connect.jpg)

The "Sterowanie ręczne" activity allows the user to manually control 3 values - PWM levels of 2 DC motors and rotation angle of the servo. The values are changed with 3 sliders. There are also 2 buttons for enabling and disabling motors rotation, a checkbox to sync speeds of both of the motors and a button to shoot out a ball which currently is not used due to lack of necessary hardware.

![manual](https://github.com/mmajchrzak557/ball-launcher/blob/master/res/manual.jpg)

The last activity - "Trening" - is used to choose where the balls should land and what kind of rotation should be applied to them. The data input by the user is converted to values mentioned earlier and sent to the controller.

![training](https://github.com/mmajchrzak557/ball-launcher/blob/master/res/training.png)
