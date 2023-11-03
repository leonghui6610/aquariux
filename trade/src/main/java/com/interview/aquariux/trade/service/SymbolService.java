package com.interview.aquariux.trade.service;

import com.interview.aquariux.trade.dtos.SymbolDto;
import com.interview.aquariux.trade.entities.Symbol;
import com.interview.aquariux.trade.entities.SymbolRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SymbolService extends BaseServiceSupport<Symbol, Long, SymbolRepo> {

    @Value("${aggregate.url:#{null}}")
    private String priceURL;
    public void refreshSymbols() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SymbolDto[]> responseEntity = restTemplate.getForEntity(priceURL, SymbolDto[].class);
            if (responseEntity != null && responseEntity.getBody() != null) {
                SymbolDto[] latestSymbolDtos = responseEntity.getBody();
                log.info("scheduled refresh symbol count: {}", latestSymbolDtos.length);
                List<Symbol> getCurrentAll = repo.findAll();
                List<Symbol> latestSymbols = Arrays.asList(latestSymbolDtos).stream().map(s -> {
                    Symbol e = new Symbol();
                    e.setName(s.getSymbol());
                    e.setAskPrice(s.getAskPrice());
                    e.setAskQty(s.getAskQty());
                    e.setBidPrice(s.getBidPrice());
                    e.setBidQty(s.getBidQty());
                    e.setCreated(Instant.now());
                    e.setAllowedTrade(false);
                    return e;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(latestSymbols)) {
                    if (CollectionUtils.isNotEmpty(getCurrentAll)) {
                        Map<String, Symbol> updateMap = getCurrentAll.stream().collect(Collectors.toMap(u -> u.getName(), u -> u));
                        List<Symbol> allSymbols = new ArrayList<>();
                        for (Symbol c : latestSymbols) {
                            if (c.getBidQty().compareTo(BigDecimal.ZERO) != 0 && c.getAskQty().compareTo(BigDecimal.ZERO) != 0 && c.getBidPrice().compareTo(BigDecimal.ZERO) != 0 && c.getAskPrice().compareTo(BigDecimal.ZERO) != 0) {
                                Symbol u = updateMap.get(c.getName());
                                if (u != null) {
                                    u.setBidQty(c.getBidQty());
                                    u.setAskQty(c.getAskQty());
                                    if (c.getBidPrice().compareTo(u.getBidPrice()) > 0) //best Bid price if new bid price higher than existing bid price
                                    {
                                        //log.info("{} new bid price {} higher than old bid price {}", u.getName(), c.getBidPrice(), u.getBidPrice());
                                        u.setBidPrice(c.getBidPrice());
                                    }
                                    if (c.getAskPrice().compareTo(u.getAskPrice()) < 0) //best Ask price if new ask price lower than existing ask price
                                    {
                                        //log.info("{} new ask price {} lower than old ask price {}", u.getName(), c.getAskPrice(), u.getAskPrice());
                                        u.setAskPrice(c.getAskPrice());
                                    }
                                    updateMap.put(c.getName(), u);
                                } else {
                                    allSymbols.add(c);
                                }
                            }
                        }
                        allSymbols.addAll(updateMap.values());
                        repo.saveAll(allSymbols);
                    } else {
                        repo.saveAll(latestSymbols);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Symbol> getAllowedProduct() {
        return repo.findAllByAllowedTradeTrue();
    }
}
