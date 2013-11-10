use po
c=db.zips
c.aggregate([ 
	{ $match: {state:{$in:["CA", "NY"]}}}, 
	{ $group: {_id : { city: "$city", state: "$state"}, tot: {$sum:"$pop"}}}
	,{ $match: {tot:{$gt:25000}}}, {$group: {_id: null, avg: {$avg:"$tot"}}}
])
