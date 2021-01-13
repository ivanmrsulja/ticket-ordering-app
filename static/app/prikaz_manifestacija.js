Vue.component("pocetna-strana", {
	data: function () {
		    return {
		      	manifestacije: {},
		      	searchParams: {naziv : "", startDate: 0, endDate: 0, lokacija: "", startPrice: 0, endPrice: 1000000, tip: "SVE", rasprodata: false, kriterijumSortiranja: "NAZIV", opadajuce: false}
		    }
	},
	template: ` 
<div>
		<h1>Prikaz manifestacija:</h1>
		
		
		<div id="mySidebar" class="sidebar">
		  <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
		  <table>
			  <tr><td colspan=2 ><input type="text" name="naziv" placeholder="Unesite naziv" v-model="searchParams.naziv" /></td></tr>
			  <tr><td colspan=2 ><input type="text" name="lokacija" placeholder="Unesite adresu" v-model="searchParams.lokacija" /></td></tr>
			  <tr><td style="color:white">Datum od:</td> <td><input type="date" name="startDate" v-model="searchParams.startDate" ></td></tr>
			  <tr><td style="color:white">Datum do:</td> <td><input type="date" name="endDate" v-model="searchParams.endDate" ></td></tr>
			  <tr><td style="color:white">Cijena od:</td> <td><input type="number" name="startPrice" v-model="searchParams.startPrice" ></td></tr>
			  <tr><td style="color:white">Cijena do:</td> <td><input type="number" name="endPrice" v-model="searchParams.endPrice" ></td></tr>
			  <tr><td style="color:white">Tip manifestacije:</td> 
			  		<td>
				  		<select name="tip" id="tip" v-model="searchParams.tip" >
						  <option value="KONCERT">KONCERT</option>
						  <option value="FESTIVAL">FESTIVAL</option>
						  <option value="POZORISTE">POZORISTE</option>
						  <option value="SVE">SVE</option>
						</select>
					</td></tr>
			  <tr><td style="color:white">Sortiraj po:</td>
			  		<td>
				  		<select name="kriterijum" id="kriterijum" v-model="searchParams.kriterijumSortiranja" >
						  <option value="NAZIV">NAZIV</option>
						  <option value="DATUM">DATUM</option>
						  <option value="CENA">CENA</option>
						  <option value="LOKACIJA">LOKACIJA</option>
						</select>
					</td></tr>
			  <tr><td style="color:white">Ukljuci rasprodate:</td> <td><input type="checkbox" name="rasprodate" v-model="searchParams.rasprodata" ></td></tr>
			  <tr><td style="color:white">Sortiraj opadajuce:</td> <td><input type="checkbox" name="opadajuce" v-model="searchParams.opadajuce" ></td></tr>
			  <tr><td colspan=2 align=center ><input type="button" name="search" value="Pretrazi" v-on:click="pretraga()" /></td></tr>
		  </table>
		</div>
		
		<div id="main">
		  <button class="openbtn" onclick="openNav()">&#9776; Pretraga</button>
		</div>
		
		
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
		pretraga: function(){
			let datumSt = (new Date(this.searchParams.startDate)).getTime();
			let datumEn = (new Date(this.searchParams.endDate)).getTime();
			let self = this;
			$.ajax({
				url: "/rest/manifestations/pretraga?naziv=" + this.searchParams.naziv + "&startDate="+datumSt + "&endDate="+datumEn + "&lokacija="+this.searchParams.lokacija + "&startPrice="+this.searchParams.startPrice + "&endPrice="+this.searchParams.endPrice + "&tip="+this.searchParams.tip + "&rasprodata="+this.searchParams.rasprodata + "&kriterijumSortiranja="+this.searchParams.kriterijumSortiranja + "&opadajuce="+this.searchParams.opadajuce,
				method: "GET",
				contentType: "application/JSON",
				success: function(data){
					if(data == "Error"){
						alert("Ispunite ispravno polja.")
					}else{
						for(d of data){
			        		var now = new Date(d.datumOdrzavanja);
			
							var day = ("0" + now.getDate()).slice(-2);
							var month = ("0" + (now.getMonth() + 1)).slice(-2);
							
							var today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
							d.datumOdrzavanja = today;
			        	}
						self.manifestacije = data;
					}
				}
			});
		}
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