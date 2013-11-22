use enron
c = db.messages
c.aggregate([
	// Grab only the from & to
	{ $project: { _id: "$_id", from: "$headers.From", toList: "$headers.To"}}
	
  // Remove duplicate from-to pairs
	, { $unwind: "$toList"}
	, { $group: { _id: {theId:"$_id", from:"$from"}, uniqueRecipients:{ $addToSet: "$toList"}}}
	
//	, { $unwind: "$uniqueRecipients"}
//	, { $project: { _id:0, from: "$_id", to: "$uniqueRecipients" }}

	// Get a count of number of emails between any given from-to pair
//	, { $group: { _id: {from:"$from", to:"$toList"}, count: {$sum: 1}}}
	
//	, { $sort: { count: 1}}
  ])
