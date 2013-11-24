use po
c=db.zips
c.aggregate([
		//{$match: {$state:{$in: ["CA", "NY"]}}}
		{$match: {$or:[{$state:"CA"}, {$state:"NY"}]}}
])
