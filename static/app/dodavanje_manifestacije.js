Vue.component("add-manifestation", {
	data: function () {
		    return {
		    }
	},
	template: ` 
<div>
		<h1>Dodaj manifestaciju: </h1>
		<table>
			<tr>
				<td> <h2>Naziv:</h2> </td> <td> <input type="text" name="naziv"/> </td>
			</tr>
			<tr>
				<td> <h2>Broj mijesta:</h2> </td> <td> <input type="number" name="mesta"/> </td>
			</tr>
			<tr>
				<td> <h2>Cijena REGULAR karte:</h2> </td> <td> <input type="number" name="cijena"/> </td>
			</tr>
			<tr>
				<td> <h2>Tip manifestacije:</h2> </td>
				<td>
					<select name="tip" id="tip" >
					  <option value="KONCERT">KONCERT</option>
					  <option value="FESTIVAL">FESTIVAL</option>
					  <option value="POZORISTE">POZORISTE</option>
					</select>
				</td> 
			</tr>
			<tr>
				<td>Datum odrzavanja:</td>
				<td> <input type="date" id="datum" name="datum"> </td>
			</tr>
			<tr>
				<td>Vreme odrzavanja:</td>
				<td> <input type="time" id="vrijeme" name="vrijeme" min="00:00" max="23:59"> </td>
			</tr>
			<tr>
				<td> <h2>Geografska sirina:</h2> </td> <td> <input type="number" name="sirina"/> </td>
			</tr>
			<tr>
				<td> <h2>Geografska duzina:</h2> </td> <td> <input type="number" name="duzina"/> </td>
			</tr>
			<tr>
				<td> <h2>Adresa:</h2> </td> <td> <input type="text" name="adresa"/> </td>
			</tr>
			<tr>
				<td> <h2>Poster:</h2> </td> <td> <input type="file" id="poster" name="poster"> </td>
			</tr>
			<tr>
				<td align=center colspan=2>
					<input type="button" value="Posalji" v-on:click="registerManif()"/>
				</td>
			</tr>
		</table>
	
</div>		  
`
	, 
	methods : {
		init : function() {
		}, 
		registerManif : function () {
			let naz = $("input[name=naziv]").val();
			let mesta = $("input[name=mesta]").val();
			let cena = $("input[name=cijena]").val();
			let tip = $('#tip option:selected').val();
			let dat = $("input[name=datum]").val();
			let date = (new Date(dat)).getTime();
			let vreme = $("input[name=vrijeme]").val();
			date += (parseInt(vreme.split(":")[0])-1)*3600000 + parseInt(vreme.split(":")[1])*60000;
			
			let sirina = $("input[name=sirina]").val();
			let duzina = $("input[name=duzina]").val();
			let addr = $("input[name=adresa]").val();
			
			
			let input = document.querySelector('input#poster');
			let file = input.files[0];
			let reader = new FileReader();
			
			reader.onloadend = function () {
			    // Since it contains the Data URI, we should remove the prefix and keep only Base64 string
			    let b64 = reader.result.replace(/^data:.+;base64,/, '');
			    console.log(b64); //-> "R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs="
			    
			    let obj = {naziv : naz,
					tipManifestacije : tip,
					datumOdrzavanja : date,
					brojMesta : mesta,
					cenaRegular : cena,
					lokacija : {geografskaSirina: sirina, geografskaDuzina: duzina, adresa: addr},
					slika : b64};
					
				$.post("/rest/manifestations/add", JSON.stringify(obj), function(data){
				if(data == "Done"){
					toast("Uspesno dodato.");
				}else{
					toast("Vec rezervisano.")
				}
			});
			    
			};

  			reader.readAsDataURL(file);
			

			//TODO: Validacija upisa
			
		} 
	},
	mounted () {
        
    }
});