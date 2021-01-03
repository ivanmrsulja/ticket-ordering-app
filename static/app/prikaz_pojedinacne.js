Vue.component("prikaz-pojedinacne", {
	data: function () {
		    return {
		      	manifestacija: {},
				user: null
		    }
	},
	template: ` 
<div>
		<h1>{{this.manifestacija.naziv}}</h1>
		
		<img :src="this.manifestacija.slika"  height=300 width=500></img>
		
		<table>
			<tr><th colspan=5> Rezervisi karte! </th></tr>
			<tr> <td> Tip karte: </td>
				<td> 
					<select name="tip_karte" id="tip_karte" >
					  <option value="REGULAR">REGULAR</option>
					  <option value="VIP">VIP</option>
					  <option value="FAN_PIT">FAN_PIT</option>
					</select> 
				</td> 
				<td> Kolicina: </td>
				<td> <input type="number" name="kolicina" min="1" :max="this.manifestacija.brojMesta"/> </td>
				<td> <input type="button" value="Rezervisi!" v-bind:disabled="this.user == null" /> </td>
			</tr>
		</table>
		
</div>		  
`
	, 
	methods : {
		setCurrent : function() {
			
		},
	},
	mounted () {
        let self = this;
		$.get("/rest/manifestations/getCurrent", function(data){
			self.manifestacija = data;
		});
		$.get("/rest/users/currentUser", function(data){
			self.user = data;
		});
    }
});