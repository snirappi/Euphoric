function send() {
	$.post(
		"messages", {
			message: $('#message').val()
		},
		function(data) {
			console.log(data);
			$('#list').html(data);
		});
};