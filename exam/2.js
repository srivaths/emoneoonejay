use enron
c = db.messages
c.aggregate([
	// Grab only the from & to
	{ $project: { from: "$headers.From", toList: "$headers.To"}}
	
  // Remove duplicate from-to pairs
	, { $unwind: "$toList"}
	, { $group: { _id: "$from", uniqueRecipients:{ $addToSet: "$toList"}}}
	
  // Expand the recipients array
	, { $unwind: "$uniqueRecipients"}
	, { $project: { _id:0, from: "$_id", to: "$uniqueRecipients" }}

	// Get a count of number of emails between any given from-to pair
	, { $group: { _id: {from:"$from", to:"$to"}, count: {$sum: 1}}}
	
	// Locate documents that have a count value > 1
	, { $match: { count: {$gt:1}}}
  ])
