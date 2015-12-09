# InteractiveLearner

Multinomial Naive Bayesian Interactive Learner by Christiaan Visscher & Dylan Ye

To use the interactive learner:
Run the Classifier class.
Add categories and provide the folder path.
After that, the interactive learner will ask you if you want to predict the probability of a document.
Provide the document's path.

For part C of the deliverable we still need to implement Feature Selection. An example of this would be removing very common and in general meaningless words such as "I", "the", "and". Removing such words can increase the accuracy of the interactive learner by not taking these into account when predicting the category.
We planned to implement this feature by adding an extra step after the normalization of the text. Filtering the normalized text would provide us a more accurate base for the predictions.

Christiaan Visscher & Dylan Ye
Projectgroup 8, module 6 Intelligent Interaction Design
University of Twente 2015/2016
