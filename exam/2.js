use enron
c = db.messages
c.aggregate([
	// Grab only the from & to
	{ $project: { from: "$headers.From", toList: "$headers.To"}}
	
  // Remove duplicate from-to pairs
	, { $unwind: "$toList"}
	, { $group: { _id: "$from", uniqueRecipients:{ $addToSet: "$toList"}}}
	, { $project: { _id:0, from: "$_id", to: "$uniqueRecipients" }}
  , { $sort: {from:1  }}
	
  // Expand the recipients array
	//, { $unwind: "$uniqueRecipients"}
	//, { $project: { _id:0, from: "$_id", to: "$uniqueRecipients" }}

	//, { $group: { _id: {from:"$from", to:"$to"}, count: {$sum: 1}}}
  ])
