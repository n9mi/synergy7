package com.synergy.binfood.repository;

import com.synergy.binfood.entity.*;
import com.synergy.binfood.utils.SeederUtil;
import com.synergy.binfood.utils.exception.SeederError;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class Repository {
    // <Key: variant.code, Value: variant>
    protected static HashMap<String, Variant> variants = new HashMap<>();

    // <Key: menu.code, Value: menu>
    protected static HashMap<String, Menu> menus = new HashMap<>();

    // <Key: `${menu.code}_${variant.code}`, Value: menuVariant>
    protected static HashMap<String, MenuVariant> menuVariants = new HashMap<>();

    // <Key: `${order.id}_${menu.code}_${variant.code}`, value: orderItem>
    protected static final HashMap<String, OrderItem> orderItems = new HashMap<>();

    // <Key: order.id, value: order>
    protected static final HashMap<String, Order> orders = new HashMap<>();

    protected static final String composite2KeyFormat = "%s_%s";
    
    protected static final String composite3KeyFormat = "%s_%s_%s";

    public void seed(SeederUtil seederUtil) throws SeederError {
        seederUtil.seedVariant();
        seederUtil.seedMenuVariants();

        Repository.variants = seederUtil.getVariantsResult();
        Repository.menus = seederUtil.getMenusResults();
        Repository.menuVariants = seederUtil.getMenuVariantsResult();
    }
}
