# CE2006_SoftwareEngineering_TrailX
Github Repo for CE2006 - Software Engineering.

TrailX is a mobile application that motivates its users to lead a healthier lifestyle by enabling them to explore new walking trails in Singapore to reconnect and restore our crumbling relationship with nature. Through TrailX, users can rediscover Singapore through the plethora of trails. TrailX will be considered complete when the application has been tested and approved for release by the SEA Organization. This project supports the smart nation goal of smart urban mobility to support their vision. 

Register a new user
- The user information is stored in the Firebase Realtime database after passing the user information through an exhaustive list of validity checks to ensure that the user has entered valid information.

Login with the new credentials (enter wrong credentials for testing)
- Firebase Authentication is used for allowing the user to Login.
- In the case of wrong credentials, a toast quoting “Authentication failed” is displayed to let the user know that an incorrect email or password was entered.
- There is also an option for the user to request for a reset password email.

Show the credentials on my account screen to show database 
- The data stored related to the user in the database is retrieved and displayed in the My profile section of the Application.

Show edit/save functionality of the credentials 
- Under the Settings tab of the Application, the user is able to view the currently saved information in their account as well as update it using a simple edit and save.

Discover new trails - Trails by distance (out of bound values for testing)
- The user is able to view Trails based on their distance. Checks have also been put into place to ensure that out of bounds distances do not crash the application.

Move to active trails to show the trail - play, pause and show the step + timer + calorie counter 
- Under the Active Trails section, the user is able to view real time information pertaining to the trail that they are currently on like weather, steps, calories and Timer.
- The user is also able to play, pause and end the trail.

Discover new trails - Trails by type
- Under Trails by Type, the user is able to view the two categories of Trails available, namely, City Trails and Nature Trails.

Move to active trails to show the trail - play, pause and show the step + timer + calorie counter + weather
- Play: start the timer, sensors to calculate steps and calculate calories real time.
- Pause - Pause the sensors and the timer.
- End - end the trail, record the trail in my trails screen 
- weather - display weather icon on the page - real time data from weather api based on geographic location. 

My trails - show that the 2 different trails have been recorded and displayed for the user
- This screen displays the trail and the image of the trail path that is recorded by the application upon completing the trail (when user clicks ‘END’ in Active trails screen) 

Music - show the functionality of the in app music player - play, pause, next song and volume. 
- The user can play music in the background, with the ability to access active trail screen’s statistics. This is coded in the backend using Kotlin, where the songs have been added within the application manually during the coding phase and it serves its purpose as an offline music player.

The application is built on Android Studio. Therefore, it is compatible with all operating systems that are built on the Android operating system. The application is built with a minimum API level of 22. The application requires access to the internet, location access as well as physical activity access and will be able to function seamlessly in the existence of the above.

Music - show the functionality of the Youtube video player API - 
- The API, when given access to by the user, allows TrailX to access the user’s Youtube app and enables them to customize their own playlist, as well as play songs of their own choice, as the mobile application runs in the background during a trail. 
