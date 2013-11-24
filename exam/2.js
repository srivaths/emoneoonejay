use enron
c = db.messages
c.aggregate([
	// Grab only the from & to
	{ $project: { from: "$headers.From", toList: "$headers.To"}}
	
  // Remove duplicate from-to pairs
	, { $unwind: "$toList"}
	, { $group: { _id: {theId:"$_id", theFrom:"$from"}, uniqueRecipients:{ $addToSet: "$toList"}}}
  , { $project: { from: "$_id.theFrom", to: "$uniqueRecipients"}}
	
	, { $unwind: "$to"}

	// Get a count of number of emails between any given from-to pair
	, { $group: { _id: {from:"$from", to:"$to"}, count: {$sum: 1}}}
  , { $sort: {count:1}}
])
