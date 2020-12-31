$(document).ready(function(){
	$("form#forma").submit(function(event){
		alert(document.getElementById("myfile").value);
		event.preventDefault();
	});
});

var aplikacija = new Vue({ 
    el: '#generic',
    data: {
        user: {name : "Jovan"}
    },
    mounted () {
        alert("Poceo");
    }
});