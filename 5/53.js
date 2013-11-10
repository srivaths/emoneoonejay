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
