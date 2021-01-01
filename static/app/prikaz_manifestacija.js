Vue.component("pocetna-strana", {
	data: function () {
		    return {
		      	total: 0,
		      	sc: {}
		    }
	},
	template: ` 
<div>
		<h1>Prikaz manifestacija:</h1>
		<a v-on:click="increment()"> Counter {{this.total}} </a>
</div>		  
`
	, 
	methods : {
		init : function() {
		},
		
		increment(){
    		this.total++;
    		this.sc["ghdgsbsh"] = this.total;
    		$.post("/rest/tickets/checkout", JSON.stringify(this.sc), function(data){
    			console.log(data);
    		})
    	}
	},
	mounted () {
        this.total = 64.0;
        this.sc = new Map();
    }
});