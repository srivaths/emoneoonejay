/*
  Removing Rural Residents In this problem you will calculate the number of 
	people who live in a zip code in the US where the city starts with a digit. 
	We will take that to mean they don't really live in a city. Once again, 
	you will be using the zip code collection you imported earlier. 
*/
use po
c=db.zips
c.aggregate([
	// Find all items where the city starts with a number.
	{$match:{ "city":/^\d.*/}},
	// Find the total population
	{$group: { _id:null, total: {$sum: "$pop"}}}
])
