Vue.component("karte-admin", {
	data: function () {
		    return {
				sc: null,
		      	total: 0
		    }
	},
	template: ` 
<div>
		<h1>Prikaz karata admin</h1>
		
	
</div>		  
`
	, 
	methods : {
		init : function() {
			this.sc = {};
			this.total = 0.0;
		}, 
		clearSc : function () {
			if (confirm('Da li ste sigurni?') == true) {
				axios
		          .post('rest/proizvodi/clearSc')
		          .then(response => (this.init()))
			}
		} 
	},
	mounted () {
        
    }
});