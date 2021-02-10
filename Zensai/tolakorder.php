<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $id_cart = $_POST['id_cart'];

    $qdelete=mysqli_query($mysqli,"DELETE FROM tbl_cart WHERE id_cart = '$id_cart' AND status = 'wait'"); 

      if($qdelete){
        $response["success"] =1;
        $response["message"] = "Data berhasil dihapus";
        echo json_encode($response);
      }
      else{
        $response["success"] =0;
        $response["message"] = "Data gagal dihapus";
        echo json_encode($response);
      }
    


	  
?>	