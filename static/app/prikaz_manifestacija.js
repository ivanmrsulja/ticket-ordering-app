Vue.component("pocetna-strana", {
	data: function () {
		    return {
				sc: null,
		      	total: 0
		    }
	},
	template: ` 
<div>
		<h1>Prikaz manifestacija:</h1>
	
</div>		  
`
	, 
	methods : {
		init : function() {
			this.sc = {};
			this.total = 0.0;
		}
	},
	mounted () {
        this.total = 64.0;
    }
});