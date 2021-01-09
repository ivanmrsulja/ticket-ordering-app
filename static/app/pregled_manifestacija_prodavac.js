Vue.component("manifestacije-prodavac", {
	data: function () {
		    return {
		    	manifestacije: {},
				selected: { lokacija: { adresa: "", geografskaSirina: "", geografskaDuzina: ""}},
				old: {},
				mode: "BROWSE"
		    }
	},
	template: ` 
<div>
		<h1>Prikaz manifestacija admin</h1>
		
		<table border=1>
			<tr bgcolor="lightgrey">
				<th>Naziv</th>
				<th>Tip manifestacije</th>
				<th>Datum odrzavanja</th>
				<th>Vrijeme odrzavanja</th>
				<th>Broj mijesta</th>
				<th>Cijena karte</th>
				<th>Adresa odrzavanja</th>
				<th>Status</th>
			</tr>
	
			<tr v-for="m in this.manifestacije" v-on:click="selectManifestacija(m)">
				<td>{{m.naziv}}</td>
				<td>{{m.tipManifestacije}}</td>
				<td>{{m.datumOdrzavanja}}</td>
				<td>{{m.vremeOdrzavanja}}</td>
				<td>{{m.brojMesta}}</td>
				<td>{{m.cenaRegular}}</td>
				<td>{{m.lokacija.adresa}}</td>
				<td>{{m.status}}</td>
			</tr>
		</table>
		<br/>
		<br/>
		
		<h1>Dodaj manifestaciju: </h1>
		<table>
			<tr>
				<td> <h2>Naziv:</h2> </td> <td> <input type="text" name="naziv" v-model="selected.naziv" disabled /> </td>
			</tr>
			<tr>
				<td> <h2>Broj mijesta:</h2> </td> <td> <input type="number" name="mesta" v-model="selected.brojMesta" /> </td>
			</tr>
			<tr>
				<td> <h2>Cijena REGULAR karte:</h2> </td> <td> <input type="number" name="cijena" v-model="selected.cenaRegular" /> </td>
			</tr>
			<tr>
				<td> <h2>Tip manifestacije:</h2> </td>
				<td>
					<select name="tip" id="tip" v-model="selected.tipManifestacije" >
					  <option value="KONCERT">KONCERT</option>
					  <option value="FESTIVAL">FESTIVAL</option>
					  <option value="POZORISTE">POZORISTE</option>
					</select>
				</td> 
			</tr>
			<tr>
				<td><h2>Datum odrzavanja:</h2></td>
				<td> <input type="date" id="datum" name="datum" v-model="selected.datumOdrzavanja" > </td>
			</tr>
			<tr>
				<td><h2>Vreme odrzavanja:</h2></td>
				<td> <input type="time" id="vrijeme" name="vrijeme" min="00:00" max="23:59" v-model="selected.vremeOdrzavanja" > </td>
			</tr>
			<tr>
				<td> <h2>Geografska sirina:</h2> </td> <td> <input type="number" name="sirina" v-model="selected.lokacija.geografskaSirina" /> </td>
			</tr>
			<tr>
				<td> <h2>Geografska duzina:</h2> </td> <td> <input type="number" name="duzina" v-model="selected.lokacija.geografskaDuzina" /> </td>
			</tr>
			<tr>
				<td> <h2>Adresa:</h2> </td> <td> <input type="text" name="adresa" v-model="selected.lokacija.adresa" /> </td>
			</tr>
			<tr>
				<td> <h2>Poster:</h2> </td> <td> <input type="file" id="poster" name="poster"> </td>
			</tr>
			<tr>
				<td align=center colspan=2>
					<input type="button" value="Posalji" v-on:click="izmeni()" v-bind:disabled="this.mode == 'BROWSE'"/>
				</td>
			</tr>
		</table>
		
</div>		  
`
	, 
	methods : {
		selectManifestacija : function(m) {
			this.selected = m;
			this.old = JSON.parse(JSON.stringify(m)); //deepcopy
			this.mode = "EDIT";
		}, 
		izmeni: function(){
			let self = this;
			
			let date = (new Date(this.selected.datumOdrzavanja)).getTime();
			date += (parseInt(this.selected.vremeOdrzavanja.split(":")[0])-1)*3600000 + parseInt(this.selected.vremeOdrzavanja.split(":")[1])*60000;
			
			let novaSlika = $("input#poster").val();
			if(novaSlika == ""){
				let obj = {
					id : this.selected.id,
					naziv : this.selected.naziv,
					tipManifestacije :  this.selected.tipManifestacije,
					datumOdrzavanja : date,
					brojMesta :  this.selected.brojMesta,
					cenaRegular :  this.selected.cenaRegular,
					lokacija : {geografskaSirina:  this.selected.lokacija.geografskaSirina, geografskaDuzina:  this.selected.lokacija.geografskaDuzina, adresa:  this.selected.lokacija.adresa},
					slika : ""};
				
				//ajax poziv
				$.ajax({
						url: "/rest/manifestations/update",
						method: "PUT",
						data: JSON.stringify(obj),
						contentType: "application/json",
						success: function(response){
							toast(response);
							if(response != "Uspesno azurirano."){
								self.selected.id = self.old.id;
								self.selected.naziv = self.old.naziv;
								self.selected.tipManifestacije = self.old.tipManifestacije;
								self.selected.datumOdrzavanja = self.old.datumOdrzavanja;
								self.selected.vremeOdrzavanja = self.old.vremeOdrzavanja;
								self.selected.brojMesta = self.old.brojMesta;
								self.selected.cenaRegular = self.old.cenaRegular;
								self.selected.lokacija = self.old.lokacija;
							}
						}
					});
				
			}else{
				let input = document.querySelector('input#poster');
				let file = input.files[0];
				let reader = new FileReader();
				
				
				reader.onloadend = function () {
				    let b64 = reader.result.replace(/^data:.+;base64,/, '');
					console.log(b64);
				    
				    let obj = {
					id : self.selected.id,
					naziv : self.selected.naziv,
					tipManifestacije :  self.selected.tipManifestacije,
					datumOdrzavanja :  date,
					brojMesta :  self.selected.brojMesta,
					cenaRegular :  self.selected.cenaRegular,
					lokacija : {geografskaSirina:  self.selected.lokacija.geografskaSirina, geografskaDuzina:  self.selected.lokacija.geografskaDuzina, adresa:  self.selected.lokacija.adresa},
					slika : b64};
						
					//ajax poziv
					$.ajax({
						url: "/rest/manifestations/update",
						method: "PUT",
						data: JSON.stringify(obj),
						contentType: "application/json",
						success: function(response){
							toast(response);
							if(response != "Uspesno azurirano."){
								self.selected.id = self.old.id;
								self.selected.naziv = self.old.naziv;
								self.selected.tipManifestacije = self.old.tipManifestacije;
								self.selected.datumOdrzavanja = self.old.datumOdrzavanja;
								self.selected.vremeOdrzavanja = self.old.vremeOdrzavanja;
								self.selected.brojMesta = self.old.brojMesta;
								self.selected.cenaRegular = self.old.cenaRegular;
								self.selected.lokacija = self.old.lokacija;
							}
						}
					});
				}
				
				reader.readAsDataURL(file);
			}
			
			
		}
	},
	mounted () {
		$.ajax({
			url: "/rest/users/currentUser",
			method: "GET",
			success: function(data){
				if(data === null || data.uloga != "PRODAVAC"){
					window.location.href = "#/login";
					flag = 1;
				}
			}
		});
		let self = this;
        $.get("/rest/manifestations/seller", function(data){
        	for(d of data){
        		var now = new Date(d.datumOdrzavanja);

				var day = ("0" + now.getDate()).slice(-2);
				var month = ("0" + (now.getMonth() + 1)).slice(-2);
				
				var today = now.getFullYear()+"-"+(month)+"-"+(day);
				var time = ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
				d.datumOdrzavanja = today;
				d.vremeOdrzavanja = time;
        	}
        	
        	self.manifestacije = data;
        	
        });
    }
});