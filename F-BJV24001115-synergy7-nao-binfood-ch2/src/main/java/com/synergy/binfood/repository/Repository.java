package com.synergy.binfood.repository;

import com.synergy.binfood.entity.*;
import com.synergy.binfood.utils.SeederUtil;
import com.synergy.binfood.utils.exception.SeederError;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class Repository {
    // <Key: variant.code, Value: variant>
    public static HashMap<String, Variant> variants = new HashMap<>();

    // <Key: menu.code, Value: menu>
    public static HashMap<String, Menu> menus = new HashMap<>();

    // <Key: `${menu.code}_${variant.code}`, Value: menuVariant>
    public static HashMap<String, MenuVariant> menuVariants = new HashMap<>();

    // <Key: `${order.id}_${menu.code}_${variant.code}`, value: orderItem>
    public static final HashMap<String, OrderItem> orderItems = new HashMap<>();

    // <Key: order.id, value: order>
    public static final HashMap<String, Order> orders = new HashMap<>();

    public static final String composite2KeyFormat = "%s_%s";
    public static final String composite3KeyFormat = "%s_%s_%s";

    public void seed(SeederUtil seederUtil) throws SeederError {
        seederUtil.seedVariant();
        seederUtil.seedMenuVariants();

        Repository.variants = seederUtil.getVariantsResult();
        Repository.menus = seederUtil.getMenusResults();
        Repository.menuVariants = seederUtil.getMenuVariantsResult();
    }
}
