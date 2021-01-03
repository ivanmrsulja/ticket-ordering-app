Vue.component("pocetna-strana", {
	data: function () {
		    return {
		      	manifestacije: {}
		    }
	},
	template: ` 
<div>
		<h1>Prikaz manifestacija:</h1>
		
		<div class="card" style="background-color:#d1d6cd; width:50%;" v-for="m in this.manifestacije">
	      <h2>{{m.naziv}}</h2>
	      <h5>{{m.tipManifestacije}}, {{m.datumOdrzavanja}}</h5>
	      <img :src="m.slika" style="height:200px;"></img>
	      <p>{{m.lokacija.adresa}}</p>
	      <p>Cena karte vec od: {{m.cenaRegular}}</p>
	    </div>
		
</div>		  
`
	, 
	methods : {
		init : function() {
			
		},
	},
	mounted () {
        let self = this;
        $.get("/rest/manifestations/welcome", function(data){
        	for(d of data){
        		var now = new Date(d.datumOdrzavanja);

				var day = ("0" + now.getDate()).slice(-2);
				var month = ("0" + (now.getMonth() + 1)).slice(-2);
				
				var today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
				d.datumOdrzavanja = today;
        	}
        	
        	self.manifestacije = data;
        	
        });
    }
});