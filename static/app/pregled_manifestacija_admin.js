Vue.component("manifestacije-admin", {
	data: function () {
		    return {
		    
		    }
	},
	template: ` 
<div>
		<h1>Prikaz manifestacija admin</h1>
		
	
</div>		  
`
	, 
	methods : {
		init : function() {
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