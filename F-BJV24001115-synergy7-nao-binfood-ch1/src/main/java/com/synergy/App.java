package com.synergy;

import com.synergy.service.RestaurantService;

public class App
{
    public static void main( String[] args )
    {
        RestaurantService rs = new RestaurantService("Binar food");
        rs.run();
    }
}
