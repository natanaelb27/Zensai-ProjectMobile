<?php
header('Content-type:application/json;charset=utf-8');
include "conn.php";

    $nama_menu = $_POST['nama_menu'];
    $username = $_POST['username'];

    $qcust=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer WHERE username = '$username'");
    if(mysqli_num_rows($qcust)>0){
    	while($r = mysqli_fetch_array($qcust)){ 	
    		$id_cust = $r['id_cust'];
    	}
    }


    $qmenu=mysqli_query($mysqli,"SELECT id_menu FROM mst_menu WHERE nama_menu = '$nama_menu'");
    if(mysqli_num_rows($qmenu)>0){
    	while($r = mysqli_fetch_array($qmenu)){ 	
    		$id_menu = $r['id_menu'];
    	}
    }

    $qcheck=mysqli_query($mysqli,"SELECT * FROM tbl_cart WHERE id_cust = '$id_cust' AND (status = 'wait' OR status = 'approved' OR status = 'waitpay')");
    if(mysqli_num_rows($qcheck)>0){
        $response["success"] =0;
        $response["message"] = "Data gagal ditambah";
        echo json_encode($response);
    }else{
        $qcart=mysqli_query($mysqli,"SELECT * FROM tbl_cart WHERE id_cust = '$id_cust' AND id_menu = '$id_menu' AND status = 'pending'");
    if(mysqli_num_rows($qcart)>0){
        while($r = mysqli_fetch_array($qcart)){     
            $quantity = $r['quantity'];
        }
        $qupdate=mysqli_query($mysqli,"UPDATE tbl_cart SET quantity = $quantity + 1 WHERE id_cust = '$id_cust' AND id_menu = '$id_menu' AND status = 'pending'");
        $response = array();

        if($qupdate){
            $response["success"] =1;
            $response["message"] = "Data berhasil ditambah";
            echo json_encode($response);
        }
        else{
            $response["success"] =0;
            $response["message"] = "Data gagal ditambah";
            echo json_encode($response);
        }
        
    } else {
        $q=mysqli_query($mysqli,"INSERT INTO tbl_cart(id_cust, id_menu, quantity, status) VALUES ('$id_cust', '$id_menu', 1, 'pending')");
        $response = array();
    
    if($q){
        $response["success"] =1;
        $response["message"] = "Data berhasil ditambah";
        echo json_encode($response);
    }
    else{
        $response["success"] =0;
        $response["message"] = "Data gagal ditambah";
        echo json_encode($response);
    }
    }

    }

    

  
    

     

	
?>