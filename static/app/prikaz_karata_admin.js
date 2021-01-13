Vue.component("karte-admin", {
	data: function () {
		    return {
		    	karte: {},
		    	searchParams: {naziv : "", startDate: 0, endDate: 0, startPrice: 0, endPrice: 1000000, tip: "SVE", status: "SVE", kriterijumSortiranja: "NAZIV", opadajuce: false}
		    }
	},
	template: ` 
<div>
		<h1>Prikaz karata admin</h1>
		
		
		<div id="mySidebar" class="sidebar">
		  <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
		  <table>
			  <tr><td colspan=2 ><input type="text" name="naziv" placeholder="Unesite naziv manifestacije" v-model="searchParams.naziv" /></td></tr>
			  <tr><td style="color:white">Datum od:</td> <td><input type="date" name="startDate" v-model="searchParams.startDate" ></td></tr>
			  <tr><td style="color:white">Datum do:</td> <td><input type="date" name="endDate" v-model="searchParams.endDate" ></td></tr>
			  <tr><td style="color:white">Cijena od:</td> <td><input type="number" name="startPrice" v-model="searchParams.startPrice" ></td></tr>
			  <tr><td style="color:white">Cijena do:</td> <td><input type="number" name="endPrice" v-model="searchParams.endPrice" ></td></tr>
			  <tr><td style="color:white">Tip karte:</td> 
			  		<td>
				  		<select name="tip" id="tip" v-model="searchParams.tip" >
						  <option value="REGULAR">REGULAR</option>
						  <option value="VIP">VIP</option>
						  <option value="FAN_PIT">FAN_PIT</option>
						  <option value="SVE">SVE</option>
						</select>
					</td></tr>
			  <tr><td style="color:white">Status karte:</td> 
			  		<td>
				  		<select name="status" id="status" v-model="searchParams.status" >
						  <option value="REZERVISANA">REZERVISANA</option>
						  <option value="ODUSTANAK">ODUSTANAK</option>
						  <option value="SVE">SVE</option>
						</select>
					</td></tr>
			  <tr><td style="color:white">Sortiraj po:</td>
			  		<td>
				  		<select name="kriterijum" id="kriterijum" v-model="searchParams.kriterijumSortiranja" >
						  <option value="NAZIV">NAZIV</option>
						  <option value="DATUM">DATUM</option>
						  <option value="CENA">CENA</option>
						</select>
					</td></tr>
			  <tr><td style="color:white">Sortiraj opadajuce:</td> <td><input type="checkbox" name="opadajuce" v-model="searchParams.opadajuce" ></td></tr>
			  <tr><td colspan=2 align=center ><input type="button" name="search" value="Pretrazi" v-on:click="pretraga()" /></td></tr>
		  </table>
		</div>
		
		<div id="main">
		  <button class="openbtn" onclick="openNav()">&#9776; Pretraga</button>
		</div>
		
		
		<table border=1>
			<tr bgcolor="lightgrey">
				<th>Kupac</th>
				<th>Ime i prezime</th>
				<th>Tip</th>
				<th>Broj mesta</th>
				<th>Status</th>
				<th>Manifestacija</th>
				<th>Datum manifestacije</th>
			</tr>
	
			<tr v-for="k in this.karte">
				<td>{{k.idKupca}}</td>
				<td>{{k.imePrezime}}</td>
				<td>{{k.tip}}</td>
				<td>{{k.brojMesta}}</td>
				<td>{{k.status}}</td>
				<td>{{k.idManifestacije}}</td>
				<td>{{k.datum}}</td>
			</tr>
		</table>
		
</div>		  
`
	, 
	methods : {
		pretraga : function() {
			let datumSt = (new Date(this.searchParams.startDate)).getTime();
			let datumEn = (new Date(this.searchParams.endDate)).getTime();
			let self = this;
			axios
			.get("/rest/tickets/pretraga?naziv=" + this.searchParams.naziv + "&startDate="+datumSt + "&endDate="+datumEn + "&lokacija="+this.searchParams.lokacija + "&startPrice="+this.searchParams.startPrice + "&endPrice="+this.searchParams.endPrice + "&tip="+this.searchParams.tip + "&status="+this.searchParams.status + "&kriterijumSortiranja="+this.searchParams.kriterijumSortiranja + "&opadajuce="+this.searchParams.opadajuce)
			.then(function(response){
				
				for(d of response.data){
	        		var now = new Date(d.datum);
	
					var day = ("0" + now.getDate()).slice(-2);
					var month = ("0" + (now.getMonth() + 1)).slice(-2);
					
					var today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
					d.datum = today;
        		}
				
				self.karte = response.data;
			})
			.catch(function(response){
				alert("Doslo je do greske.");
			});
		}
	},
	mounted () {
		$.ajax({
			url: "/rest/users/currentUser",
			method: "GET",
			success: function(data){
				if(data === null || data.uloga != "ADMIN"){
					window.location.href = "#/login";
				}
			}
		});
		let self = this;
        $.get("/rest/tickets/all", function(data){
        	
        	for(d of data){
        		var now = new Date(d.datum);

				var day = ("0" + now.getDate()).slice(-2);
				var month = ("0" + (now.getMonth() + 1)).slice(-2);
				
				var today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
				d.datum = today;
        	}
        	
        	self.karte = data;
        });
    }
});