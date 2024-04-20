package com.synergy.binfood;

import com.synergy.binfood.config.Bootstrap;
import com.synergy.binfood.view.View;

public class App
{
    public static void main( String[] args )
    {
        Bootstrap bootstrap = new Bootstrap();
        View view = new View(bootstrap);
        view.run();
    }
}
