var ALERT = {
	success : function(message) {
		Swal.fire({
			position: 'center',
	        title: message,
	        type: 'success',
	        showConfirmButton: false,
	        timer:2000,
	        width:550
	    })
	},
	error : function(message) {
		Swal.fire({
			position: 'center',
	        title: message,
	        type: 'error',
	        showConfirmButton: false,
	        timer:2000,
	        width:550
	    });
	},
	warning : function(message) {
		Swal.fire({
			position: 'center',
	        title: message,
	        type: 'warning',
	        showConfirmButton: false,
	        timer:2000,
	        width:550
	    });
	},
	confirm : function(message) {
		return Swal.fire({
			position: 'center',
			title: message,
			type: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			confirmButtonText: 'OK',
			cancelButtonColor: '#d33',
			cancelButtonText: 'Cancel',
			width:550
	    });
	}
}