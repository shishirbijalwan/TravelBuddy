
// server.js stratsup the server to set up RESTful services for the web app

// imports express package for node
var express = require('express');
var app = express();
// imports mongo package for node
var mongojs = require('mongojs');
var db = mongojs('parkingdata',['parkingdata']);
var db2 = mongojs('travelBuddy',['UserCollection']);
var db3 = mongojs('travelBuddy',['DiaryCollection']);
var db4 = mongojs('travelBuddy',['PlaceCollection']);

// imports parser package to parse json
var bodyParser = require('body-parser');

// specifies the public directory whih has UI files ie. index.html
app.use(express.static(__dirname + "/public"));

// calls when fetching data
app.use(bodyParser.json());

	
		app.get('/getUserInfo/:emailid',function(req,res){
		var temp=req.params.emailid;
		
		console.log(temp);

		db2.UserCollection.find({email : temp},function (err,docs){
			console.log(docs);
			res.json(docs);
		});
	});	
	
	app.get('/getDiaryEntries/:emailid',function(req,res){
		var temp=req.params.emailid;
		
		console.log(temp);

		db3.DiaryCollection.find({email : temp},function (err,docs){
			console.log(docs);
			res.json(docs);
		});
	});	
	
		app.get('/getAllLocation',function(req,res){
		

		db4.PlaceCollection.find(function (err,docs){
			console.log(docs);
			res.json(docs);
		});
	});	
	
			app.get('/getALocation/:name',function(req,res){
				var temp=req.params.name;


		db4.PlaceCollection.find({location : temp},function (err,docs){
			console.log(docs);
			res.json(docs);
		});
	});	
	
	app.post('/newUser',function(req,res){
		console.log('insert came');

	console.log(req.body.email);
	
	db2.UserCollection.insert(req.body,function(err,doc){
		console.log('inside success of insert');
		res.json(doc);
		
	})
});	

	app.post('/newDiaryEntry',function(req,res){
		console.log('insert Diary Entry');

	console.log(req.body);
	
	db3.DiaryCollection.insert(req.body,function(err,doc){
		console.log('inside success of insert');
		res.json(doc);
		
	})
});	
	
app.post('/deleteDiaryEntry',function(req,res){
		console.log('insert Diary Entry');

	console.log(req.body);
	
	db3.DiaryCollection.remove(req.body,function(err,doc){
		console.log('inside success of delete');
		res.json(doc);
		
	})
});	

app.post('/updateImage',function(req,res){
		console.log('insert Diary Entry');

	console.log(req.body.email);
		console.log(req.body.image);
		var useremail = req.body.email;
		var imageString = req.body.image;
		

db2.UserCollection.update({"email":useremail},{$set : {"image":imageString}},function(err,doc){
		console.log('inside success of delete');
		res.json(doc);	
	})


	
});	
			
	
app.listen(9000);
console.log('Server running on port 9000');




