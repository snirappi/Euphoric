var loading = false;
var className = "CS 30700";
var courseNum = 0;
var username = "mholm";
var calendar;
var currentStartTime;

var courses = ["CS 30700", "MA 26100", "STAT 35000", "CS 25200"];
var crns    = ["43855", "12345", "23456", "34567"];



function truncate(text, limit){
	if(text.length > limit)
		return text.substring(0, limit) + "...";
	else
		return text;
}

function scoreUp(inputID) {
	$.post(
		"scoreUp", {
			command: "scoreUp",
			user: username
			//any other data can go here
		},
		function(data) {
			console.log("UpNoted!");
		});
}

function scoreDown(inputID) {
	$.post(
		"scoreDown", {
			command: "scoreDown",
			user: username
		},
		function(data) {
			console.log("UnNoted!");
		});
}

function newMsg(text) {
	$.post(
		"http://localhost:8080/chat", {
			command: "message",
			crn: crns[courseNum],
			user: username,
			content: text
		},
		function(data) {
			console.log("Message from Server!: " + data);
			var temp = JSON.parse(data);
			addMessage(temp.content, temp.user, temp.id);
			//
		});
}

function newPost(ttitle, desc){
	$.post(
		"newPost", {
			command: "newPost",
			user: username,
			crn: crns[courseNum],
			title: ttitle,
			descrip: desc
		},
		function(data) {
			console.log(data);

		});
}

function newReply(id, text){
	$.post(
		"newReply", {
			command: "newPost",
			user: username,
			postId: id,
			crn: crns[courseNum],
			replyContent: text
		},
		function(data) {
			console.log(data);
			//
		});
}

function report(id, rreason){
	var chat = false;;
	if($('#chatroom').hasClass('hidden')){
		chat = true;
	}
	$.post(
		"reportPost", {
			command: "newPost",
			user: username,
			crn: crns[courseNum],
			postId: id,
			reason: rreason,
			type: chat
		},
		function(data) {
			console.log(data);
			//
		});
}

function login(user, pass){
	$.post(
		"login", {
			command: "login",
			user: username,
			password: pass
		},
		function(data) {
			console.log(data);
			username = user;
			//
		});
}

function refreshClasses(){
	$.post(
		"refreshClasses", {
			command: "refreshClasses",
			user: username
		},
		function(data) {
			console.log(data);

		});
}

function loadPost(id){
	$.post(
		"loadPost", {
			command: "loadPost",
			crn: crns[courseNum],
			user: username,
			postId: id
		},
		function(data) {
			console.log(data);

		});
}

function loadChat(){
	$.post(
		"loadChat", {
			command: "loadChat",
			crn: crns[courseNum],
			user: username
		},
		function(data) {
			console.log(data);
		});
	//TODO
}

function loadCalendarView(){
	$.post(
		"loadCal", {
			command: "loadCal",
			crn: crns[courseNum],
			user: username
		},
		function(data) {
			console.log(data);
			//
		});
}

function loadForum(){
	$.post(
		"loadForum", {
			command: "login",
			user: username,
			crn: crns[courseNum],
			currClass: className
		},
		function(data) {
			console.log(data);
			//
		});
}

function newEvent(name, start, end, recurring, descrip){
	$.post(
		"newEvent", {
			command: "newEvent",
			user: username,
			crn: crns[courseNum],
			title: name,
			startTime: start,
			endTime: end,
			recur: recurring,
			desc: descrip
		},
		function(data) {
			console.log(data);
			//
		});
}
function removeEvent(eid){
	$.post(
		"removeEvent", {
			command: "removeEvent",
			user: username,
			crn: crns[courseNum],
			id: eid
		})
}

function editEvent(eId, name, start, end, recurring, descrip){
 $.post(
		"editEvent", {
			command: "editEvent",
			user: username,
			id: eId,
			title: name,
			crn: crns[courseNum],
			startTime: start.format,
			endTime: end.format,
			recur: recurring,
			desc: descrip
		},
		function(data) {
			console.log(data);
			//
		});
}

function joinEvent(eId){
	$.post(
		"joinEvent", {
			command: "joinEvent",
			user: username,
			crn: crns[courseNum],
			id: eId
		},
		function(data) {
			console.log(data);
			//
		});
}

function logOut(){
	alert("not implemented yet");
}

function addPost(title, description, date, replies, score, user, eid){
	var newPost = $("<div>", {id: eid, class: "forumPost"});
	newPost.append($("<span>", {text: user, class: "author"}));
	newPost.append($("<span>", {text: score, class: "score"}));
	newPost.append($("<span>", {text: replies, class: "replies"}));
	newPost.append($("<span>", {text: date, class: "date"}));
	newPost.append($("<span>", {text: title, class: "title"}));
	newPost.append($("<span>", {text: truncate(description, 100), class: "preview"}));
	$("#forumPosts").prepend(newPost);
}

function addReply(description, date, score, user, eid){
	var newReply = $("<div>", {id: eid, class: "forumReply"});
	newReply.append($("<span>", {text: user, class: "author"}))
	newReply.append($("<span>", {text: score, class: "score"}))
	newReply.append($("<span>", {text: date, class: "date"}))
	newReply.append($("<span>", {text: description, class: "preview"}))
	newReply.append($("<span>", {text: "report", class: "reportReply"}))
	//add an upvote handler
	$("#forumReplies").append(newReply);
}

function addMessage(message, user, eid){
	var newMsg = $('<div>', {id: eid, class: "chatMessageBox"});
	var container = $("<div>", {class: "messageContainer"})
	if(username == user){
		newMsg.append($("<span>", {text: "You", class: "sender you"}));
		container.append($("<span>", {text: message, class: "toMessage"}))

	} else {
		newMsg.append($("<span>", {text: user, class: "sender"}));
		container.append($("<span>", {text: message, class: "fromMessage"}));
		newMsg.append($("<span>", {text: "Report", class: "reportButton button"}))
		//TODO: ADD HANDLER
	}
	newMsg.append(container);
	$("#chatBox").append(newMsg);
	$("#chatBox").animate({ scrollTop: $("#chatBox").prop("scrollHeight") }, 100);
}

$('#profileButton').click(function() {
	$('#darkBox').fadeIn();
	$('#userBox').fadeIn();
});

$('#settingsButton').click(function() {
	$('#darkBox').fadeIn();
	$('#settingsBox').fadeIn();
});

$('#forBut').click(function() {
	if(!$('#calendarView').hasClass("hidden")){
		$('#calendarView').fadeOut(function(){
			$('#forum').fadeIn();
			$('#forum').removeClass("hidden");
		});
		$('#calendarView').addClass("hidden");

	} else if(!$('#chatroom').hasClass("hidden")){
		$('#chatroom').fadeOut(function(){
			$('#forum').fadeIn();
			$('#forum').removeClass("hidden");
		});
		$('#chatroom').addClass("hidden");

	} else if(!$('#forumPostView').hasClass("hidden")){
		$('#forumPostView').fadeOut(function(){
			$('#forum').fadeIn();
			$('#forum').removeClass("hidden");
		});
		$('#forumPostView').addClass("hidden");

	}
});

$('#chaBut').click(function() {
	if(!$('#calendarView').hasClass("hidden")){
		$('#calendarView').fadeOut(function(){
			$('#chatroom').fadeIn();
			$('#chatroom').removeClass("hidden");

		});
		$('#calendarView').addClass("hidden");

	} else if(!$('#forum').hasClass("hidden")){
		$('#forum').fadeOut(function(){
			$('#chatroom').fadeIn();
			$('#chatroom').removeClass("hidden");

		});
		$('#forum').addClass("hidden");


	} else if(!$('#forumPostView').hasClass("hidden")){
		$('#forumPostView').fadeOut(function(){
			$('#chatroom').fadeIn();
			$('#chatroom').removeClass("hidden");

		});
		$('#forumPostView').addClass("hidden");
	}

});

$('#calBut').click(function() {
	if(!$('#forum').hasClass("hidden")){
		$('#forum').fadeOut(function(){
			$('#calendarView').fadeIn();
			$('#calendarView').removeClass("hidden");

		});
		$('#forum').addClass("hidden");

	} else if(!$('#chatroom').hasClass("hidden")){
		$('#chatroom').fadeOut(function(){
			$('#calendarView').fadeIn();
			$('#calendarView').removeClass("hidden");


		});
		$('#chatroom').addClass("hidden");

	} else if(!$('#forumPostView').hasClass("hidden")){
		$('#forumPostView').fadeOut(function(){
			$('#calendarView').fadeIn();
			$('#calendarView').removeClass("hidden");

		});
		$('#forumPostView').addClass("hidden");
	}

});

$('.forumPost').click(function() {
	loadPost( $(this).attr("id") );
	$('#forum').fadeOut(function(){
		$('#forumPostView').fadeIn();
		$('#forumPostView').removeClass("hidden");
	});
	$('#forum').addClass("hidden");
});

$('#backButton').click(function(){
	$('#forumPostView').fadeOut(function(){
		$('#forum').fadeIn();
		$('#forum').removeClass("hidden");
	});
	$('#forumPostView').addClass("hidden");
})

$('#loginButton').click(function() {
	$('#darkBox').fadeIn();
	$('#loginBox').fadeIn();
});


$('#newPostButton').click(function() {
	$('#darkBox').fadeIn();
	$('#newPostBox').fadeIn();
});

$('#submitPost').click(function() {
	if($('#newPostTitle').val() != '' && $('#newPostContent').val() != ''){
		$('#darkBox').fadeOut();
		$('#newPostBox').fadeOut();
		//title, description, date, replies, score, user, id
		newPost($('#newPostTitle').val(), $('#newPostContent').val())
		//TEMPORARY
		addPost($('#newPostTitle').val(),
				$('#newPostContent').val(),
				new Date,
				0,
				0,
				username,
				Math.floor(Math.random()*1000000));
	}
});

$('#submitReply').click(function(){
	$('#darkBox').fadeOut();
	$('#newReplyBox').fadeOut();
	//desc, date, score, user, id
	addReply(
			$('#newReplyContent').val(),
			new Date,
			0,
			username,
			Math.floor(Math.random()*1000000));
});

$('#classButton #classMenu li').click(function() {
	className = $(this).text();
	alert(className);
})

$('#darkBox').click(function(e) {
	if(loading)
		return;
	$('#loginBox').fadeOut();
	$('#userBox').fadeOut();
	$('#darkBox').fadeOut();
	$('#settingsBox').fadeOut();
	$('#reportBox').fadeOut();
	$('#newPostBox').fadeOut();
	$('#newReplyBox').fadeOut();
	$('#newEventBox').fadeOut();
	$('#eventOpsBox').fadeOut();
});


$('#chatInput').keypress(function(e) {
	if( e.which == 13 && $('#chatInput').val() != '') {
		newMsg($('#chatInput').val());
		//TEMPORARY
		// if($("#fakeUser").is(':checked'))
		// 	addMessage($('#chatInput').val(), "Jerg", Math.floor(Math.random()*1000000))
		// else
		// 	addMessage($('#chatInput').val(), username, Math.floor(Math.random()*1000000))

		$('#chatInput').val('');
	}
});

$('#sendInputButton').click(function(){
	if($('#chatInput').val() != ''){
		newMsg($('#chatInput').val());
		// //TEMPORARY
		// if($("#fakeUser").is(':checked'))
		// 	addMessage($('#chatInput').val(), "Jerg", Math.floor(Math.random()*1000000))
		// else
		// 	addMessage($('#chatInput').val(), username, Math.floor(Math.random()*1000000))

		$('#chatInput').val('');
	}
})

$('.reportButton').click(function(){
	$('#darkBox').fadeIn();
	$('#reportBox').fadeIn();
	$('#reportedSender').text($(this).parent().children('.sender').text());
	$('#reportedMessage').text($(this).parent().children('.fromMessage').text());

})

$('.reportPost').click(function() {
	$('#darkBox').fadeIn();
	$('#reportBox').fadeIn();
	$('#reportedSender').text($(this).parent().children('.author').text());
	$('#reportedMessage').text(truncate($(this).parent().children('.preview').text(), 100));

})

$('#passwordBox').keypress(function(e) {
	if( e.which == 13) {
		alert('hi ' + $('#usernameBox').val());
		$('#loginBox').fadeOut();
		$('#loadingBox').fadeIn(300);
		$('#loadingBox').fadeOut(300);
		$('#darkBox').fadeOut();
		loading = true;
		login($('#usernameBox').val(),$('#passwordBox').val());
		$('#splash').addClass('hidden');
		$('#calendarView').removeClass('hidden');
		$('#calendarView').fadeIn();
		loading = false;
		username = $('#usernameBox').val();
	}
})


//function newEvent(name, start, end, recurring, descrip){

$('#submitEvent').click(function(){
	var endTime = moment(currentStartTime);
	endTime.hour(currentStartTime.hour() + parseInt($('#newEventDuration').val()));

	newEvent($('#newEventTitle').val(),
			 currentStartTime.format(),
			 endTime.format(),
			 $('#newEventRecurring').is(":checked"),
			 $('#newEventContent').val()
			)
	$('#darkBox').fadeOut();
	$('#newEventBox').fadeOut();
	calendar.fullCalendar('renderEvent',
		{
			title: $('#newEventTitle').val(),
			start: currentStartTime,
			end: endTime,
			creator: username,
			description: $('#newEventContent').val()
		},
		true // make the event "stick"
	);
})


$('#darkBox *').click(function(e) {
	e.stopPropagation();
});

//Forum View Code

$('.score').click(function(e) {
	if($(this).hasClass("voted")){
		$(this).text(parseInt($(this).text()) - 1);
		$(this).removeClass("voted");
		scoreUp($(this).parent().attr("id"));
	} else {
		$(this).text(parseInt($(this).text()) + 1);
		$(this).addClass("voted");
		scoreDown($(this).parent().attr("id"));
	}
})

$('#newReplyButton').click(function() {
	$('#darkBox').fadeIn();
	$('#newReplyBox').fadeIn();
})

$('#joinEvent').click(function(){
	//joinEvent($(this));
	$('#darkBox').fadeOut();
	$('#eventOpsBox').fadeOut();
	$(this).parent().data('event').backgroundColor = "#77B7FC";
})

$('#removeEvent').click(function(){
	calendar.fullCalendar('removeEvents',$(this).parent().data("event").id);
	$('#darkBox').fadeOut();
	$('#eventOpsBox').fadeOut();
})

$('#reportEvent').click(function(){
	$('#reportedSender').text($('#eventAuthor').text());
	$('#reportedMessage').text($('#eventOpTitle').text());
	$('#reportBox').data("item", $(this).parent().data("event"))
	$('#eventOpsBox').hide();
	$('#reportBox').fadeIn();
})

$('#reportCheating').click(function(){
	report($(this).parent().data("item").id, "cheating")
	$('#darkBox').fadeOut();
	$('#reportBox').fadeOut();
})

$('#reportOther').click(function(){
	report($(this).parent().data("item").id, "other");
	$('#darkBox').fadeOut();
	$('#reportBox').fadeOut();
})

$('#hideEvent').click(function(){
	calendar.fullCalendar('removeEvents',$(this).parent().data("event").id);
	//Need a local hiding system
})

//Calendar code

$(document).ready(function() {
	$('#loadingBox').fadeOut();
	$('#loginBox').fadeOut();
	$('#userBox').fadeOut();
	$('#darkBox').fadeOut();
	$('#settingsBox').fadeOut();
	$('#reportBox').fadeOut();
	$('#newPostBox').fadeOut();
	$('#newReplyBox').fadeOut();
	$('#newEventBox').fadeOut();
	$('#eventOpsBox').fadeOut();

	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();

    // page is now ready, initialize the calendarView...
    calendar = $('#calendar').fullCalendar({
    	header:
				{
					left: 'prev,next today',
					center: 'title',
					right: 'month,agendaWeek,agendaDay'
				},
		selectable: true,
		selectHelper: true,
		defaultView: 'agendaWeek',

		dayClick: function(date, jsEvent, view) {
			$('#darkBox').fadeIn();
	        $('#newEventBox').fadeIn();
	        if(date.minutes() == 0)
	        	$('#newEventTimeScale').text(date.format("h:mm"));
	    	else
	        	$('#newEventTimeScale').text(date.hours() + ":" + date.minutes());
	        currentStartTime = date;
    	},
    	eventClick: function(calEvent, jsEvent, view) {
	        $('#darkBox').fadeIn();
	        $('#eventOpsBox').fadeIn();
	        $('#eventOpsBox').data("event",calEvent)
	        $('#eventOpTitle').addClass(calEvent.id);
	        $('#eventOpTitle').text(calEvent.title);
	        $('#eventAuthor').text(calEvent.creator);
	        $('#eventOpTimeScale').text(calEvent.start.format("h:mm") + " - " + calEvent.end.format("h:mm"));
	        $('#eventOpDesc').text(calEvent.description);
	        if(calEvent.creator == username){
	        	$('#removeEvent').removeClass('hidden')
	        	$('#removeEvent').show();
	        }

    	},

		// select: function(start, end, allDay)
		// {
		// 	var title = prompt('Event Title:');
		// 	if (title){
		// 		calendar.fullCalendar('renderEvent',
		// 			{
		// 				title: title,							start: start,
		// 				end: end,
		// 				allDay: allDay
		// 			},
		// 			true // make the event "stick"
		// 		);
		// 	}
		// 	calendar.fullCalendar('unselect');
		// },
		editable: true,
    })

});