Vue.component("prikaz-pojedinacne", {
	data: function () {
		    return {
		      	manifestacija: {},
				user: null,
				commentable: false,
				rating: 1.0,
				komentari: {},
				ukupnaOcena: 0.0
		    }
	},
	template: ` 
<div>
		<h1>{{this.manifestacija.naziv}}</h1>
		<h3>{{this.manifestacija.tipManifestacije}}</h3>
		<h4 style="opacity:70%">({{this.manifestacija.status}})</h4>
		
		<div class="gallery">
		<img :src="this.manifestacija.slika"  height=300 width=500></img>
			<div>
				<strong>Pocetak: {{this.manifestacija.datumOdrzavanja}} u {{this.manifestacija.vremeOdrzavanja}}h | Ostalo {{this.manifestacija.brojMesta}} karata!</strong>
			</div>
		</div>
		
		</br>
		</br>
		
		<table>
			<tr align=center><th colspan=5> Rezervisi karte! Cena vec od: {{this.manifestacija.cenaRegular}} RSD </th></tr>
			<tr> <td> Tip karte: </td>
				<td> 
					<select name="tip_karte" id="tip_karte" >
					  <option value="REGULAR">REGULAR</option>
					  <option value="VIP">VIP</option>
					  <option value="FAN_PIT">FAN_PIT</option>
					</select> 
				</td> 
				<td> Kolicina: </td>
				<td> <input type="number" name="kolicina" min="0" :max="this.manifestacija.brojMesta"/> </td>
				<td> <input type="button" value="Rezervisi!" v-on:click="addToCart()" v-bind:disabled="this.user == null || this.manifestacija.status == 'NEAKTIVNO' || this.manifestacija.datumOdrzavanja == null || (new Date(this.manifestacija.datumOdrzavanja.split('-')[2]+'-'+this.manifestacija.datumOdrzavanja.split('-')[1]+'-'+this.manifestacija.datumOdrzavanja.split('-')[0])).getTime() <= (Date.now()) || this.manifestacija.brojMesta == 0" /> </td>
			</tr>
		</table>
		
		<br/>
		
		<h4 align: center>Nalazimo se na:</h4>
		
		<div id="map" class="map"></div>
		<br/>
		
		
		<h3 v-if="this.ukupnaOcena != 0">Ukupna ocjena: {{this.ukupnaOcena}}</h3>
		<br/>
		
		<h2 v-bind:hidden="!this.commentable" >Ostavi komentar:</h2>
	    <textarea v-bind:hidden="!this.commentable" class="comment" name="komentar" placeholder="Unesite komentar" v-bind:disabled="!this.commentable"></textarea>
	    <br/>
	    <form v-bind:hidden="!this.commentable" class="rating">
		  <label>
		    <input type="radio" name="stars" value="1" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		  </label>
		  <label>
		    <input type="radio" name="stars" value="2" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		  </label>
		  <label>
		    <input type="radio" name="stars" value="3" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>   
		  </label>
		  <label>
		    <input type="radio" name="stars" value="4" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		  </label>
		  <label>
		    <input type="radio" name="stars" value="5" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		  </label>
		</form>
		<br/>
	    <input type="button" name="submit" value="Postavi" v-bind:hidden="!this.commentable" v-on:click="postComment()" >
	    
	    <br/>
	    <br/>
	    
	    <h2 v-bind:hidden="Object.keys(this.komentari).length == 0" >Komentari</h2>
	    
	    <br/>
	    
	    <div style="border: 5px solid red; width: 15%" v-for="k in komentari">
	    	<p>Korisnik: {{k.kupac.username}}</p>
	    	<p>{{k.tekst}}</p>
	    	<p>Ocena: {{k.ocena}}</p>
	    </div>
		
</div>		  
`
	, 
	methods : {
		showMap : function(){
			let self = this;
			var map = new ol.Map({
		        target: 'map',
		        layers: [
		          new ol.layer.Tile({
		            source: new ol.source.OSM()
		          })
		        ],
		        view: new ol.View({
		          center: ol.proj.fromLonLat([self.manifestacija.lokacija.geografskaDuzina, self.manifestacija.lokacija.geografskaSirina]),
		          zoom: 16
		        })
		      });
		     var layer = new ol.layer.Vector({
			     source: new ol.source.Vector({
			         features: [
			             new ol.Feature({
			                 geometry: new ol.geom.Point(ol.proj.fromLonLat([self.manifestacija.lokacija.geografskaDuzina, self.manifestacija.lokacija.geografskaSirina]))
			             })
			         ]
			     })
			 });
			 map.addLayer(layer);
		},
		addToCart : function() {
			let man = this.manifestacija.id;
			let tip = $('#tip_karte option:selected').val();
			let amount = $('input[name=kolicina]').val();
			
			let sci = {idManifestacije: man, naziv: this.manifestacija.naziv, kolicina: amount, tipKarte: tip};
			
			$.post("rest/tickets/addToCart", JSON.stringify(sci), function(data){
				alert(data);
			})
			
		},
		postComment : function(){
			let com = {tekst: $("textarea[name=komentar]").val(), ocena: this.rating};
			
			$.ajax({
			url: "/rest/comments/postComment",
			data: JSON.stringify(com),
			method: "POST",
			contentType: "application/json",
			success: function(response){
				alert(response);
			},
			error: function(response){
				alert("Doslo je do greske.");
			}
			});
			
		}
	},
	mounted () {
        let self = this;
		$.get("/rest/manifestations/getCurrent", function(data){
			
			let now = new Date(data.datumOdrzavanja);

			let day = ("0" + now.getDate()).slice(-2);
			let month = ("0" + (now.getMonth() + 1)).slice(-2);
			
			let today = (day)+"-"+(month)+"-"+now.getFullYear();
			let time = ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
			data.datumOdrzavanja = today;
			data.vremeOdrzavanja = time;
			
			
			self.manifestacija = data;
			
			self.showMap();
		});
		$.get("/rest/users/currentUser", function(data){
			self.user = data;
		});
		$.get("/rest/manifestations/commentable", function(data){
			self.commentable = data;
		});
		$(":radio").change(function(){
			self.rating = this.value;
		});
		$.get("/rest/comments/forPost", function(data){
			self.komentari = data;
			let counter = 0;
			for(d of data){
				self.ukupnaOcena += d.ocena;
				counter ++;
				d.tekst = d.tekst.replaceAll("_", "\n");
			}
			if (counter != 0){
				self.ukupnaOcena = self.ukupnaOcena / counter;
			}
		});
    }
});