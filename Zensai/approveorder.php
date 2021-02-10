<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $id_cart = $_POST['id_cart'];
  
    $qcart=mysqli_query($mysqli,"SELECT * FROM tbl_cart WHERE id_cart = '$id_cart' AND status = 'wait'");
    if($qcart){  
      $qupdate=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'approved' WHERE id_cart = '$id_cart' AND status = 'wait'");
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
      
    } 
   

    


	  
?>	