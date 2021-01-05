Vue.component("register-seller", {
	data: function () {
		    return {
		    }
	},
	template: ` 
<div>
		<h1>Registracija prodavaca: </h1>
		<table>
			<tr>
				<td> <h2>Username:</h2> </td> <td> <input type="text" name="username"/> </td>
			</tr>
			<tr>
				<td> <h2>Password:</h2> </td> <td> <input type="text" name="pass"/> </td>
			</tr>
			<tr>
				<td> <h2>Ime:</h2> </td> <td> <input type="text" name="ime"/> </td>
			</tr>
			<tr>
				<td> <h2>Prezime:</h2> </td> <td> <input type="text" name="prezime"/> </td>
			</tr>
			<tr>
				<td> <h2>Pol:</h2> </td>
				<td>
					<select name="pol" id="pol" >
					  <option value="ZENSKI">ZENSKI</option>
					  <option value="MUSKI">MUSKI</option>
					</select>
				</td> 
			</tr>
			<tr>
				<td>Datum rodjenja:</td>
				<td> <input type="date" id="birthday" name="birthday"> </td>
			</tr>
			<tr>
				<td align=center colspan=2>
					<input type="button" value="Posalji" v-on:click="registerUser()"/>
				</td>
			</tr>
		</table>
	
</div>		  
`
	, 
	methods : {
		init : function() {
		}, 
		registerUser : function () {
			let usr = $("input[name=username]").val();
			let pas = $("input[name=pass]").val();
			let ime = $("input[name=ime]").val();
			let prz = $("input[name=prezime]").val();
			let pol = $('#pol option:selected').val();
			let dat = $("input[name=birthday]").val();
			let date = (new Date(dat)).getTime();

			//TODO: Validacija upisa
			
			newUser = {username: usr, password: pas, ime: ime, prezime : prz, pol: pol, datumRodjenja: date};
			let addr = '/rest/users/registerSeller';
			
			axios
    		.post(addr, newUser)
    		.then(function(response){
				if(response.data == "Done"){
					toast("Uspesno ste kreirali nalog.");
				}else{
					toast("Vec postoji korisnik sa tim kredencijalima, pokusajte ponovo.");
				}	
    		});

		} 
	},
	mounted () {
        
    }
});