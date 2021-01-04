Vue.component("pocetna-strana", {
	data: function () {
		    return {
		      	manifestacije: {}
		    }
	},
	template: ` 
<div>
		<h1>Prikaz manifestacija:</h1>
		
		<div class="card" v-for="m in this.manifestacije">
		  <div class="post-container">
	      <div class="post-thumb"><img :src="m.slika" style="height:200px;"></img></div>
		  <div class="post-content">
	      <h2 style="margin-bottom:6px">{{m.naziv}}</h2>
	      <h5>{{m.tipManifestacije}}, {{m.datumOdrzavanja}}</h5>
	      
	      <p>{{m.lokacija.adresa}}</p>
	      <p>Cena karte vec od: {{m.cenaRegular}}</p>
	      <input type="button" class="button1" value="Vise informacija" v-on:click="setCurrent(m)" />
	      </div></div>
	    </div>
		
</div>		  
`
	, 
	methods : {
		setCurrent : function(m) {
			let current = JSON.parse(JSON.stringify(m)); //deepcopy :O
			current.datumOdrzavanja = current.datumOdrzavanja.split(" ")[0];
			let date = (new Date(current.datumOdrzavanja)).getTime();
			current.datumOdrzavanja = date;
			$.post("/rest/manifestations/setCurrent", JSON.stringify(current), function(data){
				window.location.href = "#/prikaz" ;
			});
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