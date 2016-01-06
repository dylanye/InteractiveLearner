# InteractiveLearner

Multinomial Naive Bayesian Interactive Learner by Christiaan Visscher & Dylan Ye

System requirements:
Java JDK 1.8 installed
Eclipse Mars or Intellij 15.0.1 installed

How to install:
import the zip file into the eclipse or intellij

To use the interactive learner:
Run the Classifier class.
Add categories and provide the folder path to train the interactive learner. Keep in mind that the training of whole folders and adding categories can only be done during this stage.

After that, the interactive learner will ask you if you want to predict the probability of a single document (apply) or determine it's accuracy (acc).

Accuracy: If you choose to determine its accuracy, you will be asked to provide the path of a test folder.
The program will do the calculations now over multiple files and when it is done it will show you the results and ask you if you want to continue and what you want to do next.

Apply: If you choose to apply, you will be asked to provide the location of the test file.
The program will then do the calculations for this single file and show you the result.
Next it will ask you if the result is correct, if it is not correct it will ask you to give the correct result.
Afterwards the program will update his training set(read: learn from the file you just used) so the program can give better predictions for the next calculation. The interactive learner will keep these ‘lessons’ until closed.

When it is done updating it will show you the results and ask you if you want to continue and what you want to do next. 

Christiaan Visscher & Dylan Ye
Projectgroup 8, module 6 Intelligent Interaction Design
University of Twente 2015/2016
