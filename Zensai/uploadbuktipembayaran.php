<?php
header('content-type:application/json;charset=utf-8');
include "conn.php";
 

	$image = $_POST['imageurl'];
	$username = $_POST['username'];
	$qcust=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer WHERE username = '$username'");
	if(mysqli_num_rows($qcust)>0){
		while($r = mysqli_fetch_array($qcust)){   
			$id_cust = $r['id_cust'];
		}
	}
  $total_pembayaran = $_POST['totalpembayaran'];
 	$id = uniqid($username, true);
 	$p = "buktipembayaran/$id.jpeg";
 	$path = "https://10.0.2.2/Zensai/$p";
 	$checkrefused=mysqli_query($mysqli, "SELECT * FROM tbl_pembayaran WHERE id_cust='$id_cust' AND status='refused'");
 	if(mysqli_num_rows($checkrefused)>0){
 		file_put_contents($p,base64_decode($image));
	 		$qpem=mysqli_query($mysqli,"SELECT id_pembayaran FROM tbl_pembayaran WHERE id_cust = '$id_cust' AND status = 'refused'");
			if(mysqli_num_rows($qpem)>0){
				while($r = mysqli_fetch_array($qpem)){   
					$id_pembayaran = $r['id_pembayaran'];
				}
			}
 		$updatebukti=mysqli_query($mysqli, "UPDATE tbl_pembayaran SET status='wait', bukti_pembayaran= '".$path."' WHERE id_pembayaran='$id_pembayaran'");
 		$qupdate=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'waitpay' WHERE id_pembayaran='$id_pembayaran' AND status='approved'");
 	}else{
 		$sqli=mysqli_query($mysqli, "INSERT INTO tbl_pembayaran(id_cust, total_pembayaran, bukti_pembayaran, status) VALUES('$id_cust', $total_pembayaran, '".$path."', 'pending')");
	 	if($sqli){
	 		file_put_contents($p,base64_decode($image));
	 		$qpem=mysqli_query($mysqli,"SELECT id_pembayaran FROM tbl_pembayaran WHERE id_cust = '$id_cust' AND status = 'pending'");
			if(mysqli_num_rows($qpem)>0){
				while($r = mysqli_fetch_array($qpem)){   
					$id_pembayaran = $r['id_pembayaran'];
				}
			}
			$metodetransfer=mysqli_query($mysqli,"SELECT id_mtdpembayaran FROM tbl_paymentmethod WHERE nama_metode='transfer'");
			if(mysqli_num_rows($metodetransfer)>0){
				while($r = mysqli_fetch_array($metodetransfer)){   
					$id_mtdpembayaran = $r['id_mtdpembayaran'];
				}
			}
	 		$qupdate=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'waitpay', id_pembayaran = '$id_pembayaran' WHERE id_cust='$id_cust' AND status='approved'");
	 		$qupdate2=mysqli_query($mysqli,"UPDATE tbl_pembayaran SET id_mtdpembayaran = '$id_mtdpembayaran', status = 'wait' WHERE id_pembayaran='$id_pembayaran' AND status='pending'");
	 	}
 	}
 	
    


 	

?>