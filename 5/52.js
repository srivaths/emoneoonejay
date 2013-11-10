/*
Crunching the Zipcode dataset
Please download the zips.json dataset and import it into a collection of your choice. 

Please calculate the average population of cities in California (abbreviation CA)
 and New York (NY) (taken together) with populations over 25,000. 

For this problem, assume that a city name that appears in more than one state 
represents two separate cities. 

Please round the answer to a whole number. 
*/
use po
c=db.zips
c.aggregate([ 
	{ $match: {state:{$in:["CA", "NY"]}}}, 
	{ $group: {_id : { city: "$city", state: "$state"}, tot: {$sum:"$pop"}}}
	,{ $match: {tot:{$gt:25000}}}, {$group: {_id: null, avg: {$avg:"$tot"}}}
])
