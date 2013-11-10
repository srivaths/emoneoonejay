/*
Who's the easiest grader on campus?
In this problem you will be analyzing a dataset of student grades. 
Please import grades_5-3.js into a database and collection of your choice. 

There are documents for each student (student_id) across a variety of 
classes (class_id). Note that not all students in the same class have the 
same exact number of assessments. Some students have three homework 
assignments, etc. 

Your task is to calculate the class with the best average student 
performance. This involves calculating an average for each student in each
class of all non-quiz assessments and then averaging those numbers to get 
a class average. To be clear, each student's average includes only exams 
and homework grades. Don't include their quiz scores in the calculation. 

What is the class_id which has the highest average student perfomance? 

Hint/Strategy: You need to group twice to solve this problem. You must 
figure out the GPA that each student has achieved in a class and then 
average those numbers to get a class average. After that, you just need 
to sort. The hardest class is class_id=2. Those students achieved a 
class average of 37.6
*/
use 53
c=db.grades
c.aggregate([
	// Make sure we can process all elements in the scores array
	{ $unwind: "$scores"},
	// We are not interested in quiz scores
	{ $match: { "scores.type": {$ne: "quiz"}}},
	// Get the average for a student in a class
	{ $group: { _id: {class:"$class_id", student: "$student_id"}, avg: {$avg: "$scores.score"}}},
	// Get the average across all classes
	{ $group: { _id: "$_id.class", avg: {$avg: "$avg"}}},
	// List in descending order
	{ $sort: { avg:-1}},
	// Get only the highest average
	{ $limit:1}
])
