use enron
c=db.messages
c.aggregate([
	{$match:{"headers.From": "andrew.fastow@enron.com"}}
	,{$project: {_id:1, 'from': '$headers.From', 'toList':'$headers.To'}}
	,{ $unwind: "$toList"} 
	,{$project: {_id:1, from: 1, 'to':'$toList'}}
	,{$match:{"to": "jeff.skilling@enron.com"}}
	,{$group: { _id: null, count: { $sum: 1 }}}
])
