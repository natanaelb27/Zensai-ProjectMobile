<?php
header('content-type:application/json;charset=utf-8');
include "conn.php";
$username = $_POST['username'];
$paymentmethod = $_POST['paymentmethod'];
$qcust=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer WHERE username = '$username'");
if(mysqli_num_rows($qcust)>0){
	while($r = mysqli_fetch_array($qcust)){   
		$id_cust = $r['id_cust'];
	}
}
$metodetransfer=mysqli_query($mysqli,"SELECT id_mtdpembayaran FROM tbl_paymentmethod WHERE nama_metode='$paymentmethod'");
if(mysqli_num_rows($metodetransfer)>0){
	while($r = mysqli_fetch_array($metodetransfer)){   
		$id_mtdpembayaran = $r['id_mtdpembayaran'];
	}
}
$totalharga = mysqli_query($mysqli, "SELECT SUM(mst_menu.harga * tbl_cart.quantity) AS total FROM mst_menu JOIN tbl_cart ON mst_menu.id_menu = tbl_cart.id_menu WHERE id_cust = '$id_cust' AND status = 'approved'");
if(mysqli_num_rows($totalharga)>0){
	while($r = mysqli_fetch_array($totalharga)){   
		$total_pembayaran = $r['total'];
	}
}
$sqli=mysqli_query($mysqli, "INSERT INTO tbl_pembayaran(id_cust, id_mtdpembayaran, total_pembayaran, status) VALUES('$id_cust', '$id_mtdpembayaran', $total_pembayaran, 'pending')");
if($sqli){
	$qpem=mysqli_query($mysqli,"SELECT id_pembayaran FROM tbl_pembayaran WHERE id_cust = '$id_cust' AND status = 'pending'");
	if(mysqli_num_rows($qpem)>0){
		while($r = mysqli_fetch_array($qpem)){   
			$id_pembayaran = $r['id_pembayaran'];
		}
	}
	$qupdate=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'waitpay', id_pembayaran = '$id_pembayaran' WHERE id_cust='$id_cust' AND status='approved'");
	$qupdate2=mysqli_query($mysqli,"UPDATE tbl_pembayaran SET status = 'wait' WHERE id_pembayaran='$id_pembayaran' AND status='pending'");
}

?>