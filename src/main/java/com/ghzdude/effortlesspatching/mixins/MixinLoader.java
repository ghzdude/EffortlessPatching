package com.ghzdude.effortlesspatching.mixins;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class MixinLoader implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        List<String> list = new ArrayList<>();
        list.add("mixins.effortlesspatching.json");
        return list;
    }
}
