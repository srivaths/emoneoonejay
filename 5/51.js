use blog
c=db.posts
c.aggregate([
		{
			$unwind: "$comments"
		},
		{
			$group:{
							_id:"$comments.author", 
							count:{$sum:1}
						}
		},
		{
			$sort: { count: 1}
		},
		{
			$project: {
							_id:0,
							"author": "$_id",
							count: 1
			}
		}
		])
