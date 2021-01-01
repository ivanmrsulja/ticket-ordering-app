Vue.component("update-user", {
	data: function () {
		    return {
		    }
	},
	template: ` 
<div>
		<h1>Korisnicki profil: </h1>
		<table>
			<tr>
				<td> <h2>Username:</h2> </td> <td> <input type="text" name="username" disabled /> </td>
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
					<input type="button" value="Posalji" v-on:click="updateUser()"/>
				</td>
			</tr>
		</table>
	
</div>		  
`
	, 
	methods : {
		init : function() {
		}, 
		updateUser : function () {
			let usr = $("input[name=username]").val();
			let pas = $("input[name=pass]").val();
			let ime = $("input[name=ime]").val();
			let prz = $("input[name=prezime]").val();
			let pol = $('#pol option:selected').val();
			let dat = $("input[name=birthday]").val();
			let date = (new Date(dat)).getTime();
			
			updatedUser = {username: usr, password: pas, ime: ime, prezime : prz, pol: pol, datumRodjenja: date};
			let addr = '/rest/users/updateUser';
			
			$.ajax({
				  url: addr,
				  type: 'PUT',
				  data: JSON.stringify(updatedUser),
				  success: function(data) {
				    alert('Uspesno azurirano.');
				  }
				});

		} 
	},
	mounted () {
			
			$.get("rest/users/currentUser", function(data){
				 $("input[name=username]").val(data.username);
				$("input[name=pass]").val(data.password);
				$("input[name=ime]").val(data.ime);
				$("input[name=prezime]").val(data.prezime);
				$('#pol').val(data.pol);
				
				var now = new Date(data.datumRodjenja);

				var day = ("0" + now.getDate()).slice(-2);
				var month = ("0" + (now.getMonth() + 1)).slice(-2);
				
				var today = now.getFullYear()+"-"+(month)+"-"+(day) ;
				
				$("input[name=birthday]").val(today);
				
			});
    }
});