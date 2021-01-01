const PrikazManifestacijaPocetna = { template: '<pocetna-strana></pocetna-strana>' }
const Registracija = { template: '<register-user></register-user>' }
const Login = { template: '<login-user></login-user>' }
const Profil = { template: '<update-user></update-user>' }
const ManifestacijeAdmin = { template: '<manifestacije-admin></manifestacije-admin>' }
const KorisniciAdmin = { template: '<korisnici-admin></korisnici-admin>' }
const KarteAdmin = { template: '<karte-admin></karte-admin>' }
const KomentariAdmin = { template: '<komentari-admin></komentari-admin>' }


const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: PrikazManifestacijaPocetna},
	    { path: '/register', component: Registracija },
	    { path: '/login', component: Login },
	    { path: '/adminManif', component: ManifestacijeAdmin },
	    { path: '/adminUsers', component: KorisniciAdmin },
	    { path: '/adminTickets', component: KarteAdmin },
	    { path: '/adminComment', component: KomentariAdmin },
	    { path: '/profile', component: Profil }
	  ]
});


var app = new Vue({
	router,
	el: '#webShop',
	data: {
        korisnik: undefined,
    },
	mounted () {
		this.korisnik = {uloga : "GOST"};
		this.$root.$on('sendingUser', (data) => {
			this.korisnik = data;
		});
    },
     methods: {
    	logout : function() {
    		let self = this;
    		axios
    			.get("/rest/users/logout")
    			.then(function(resp){
    				if(resp.data == "Done"){
    					self.korisnik = {uloga : "GOST"};
    					window.location.href = "#/";
    					self.$root.$emit('loggingUserOut', self.korisnik);
    				}
    			});
    	}   
    }
});