package growup.spring.springserver.record.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;

    @Column(name = "billing_method")
    private String billingMethod;

    @Column(name = "sales_method")
    private String salesMethod;

    @Column(name = "ad_type")
    private String adType;

    @Column(name = "campaign_id")
    private String campaignId;

    @Column(name = "campaign_name")
    private String campaignName;

    @Column(name = "ad_group")
    private String adGroup;

    @Column(name = "product_advertised")
    private String productAdvertised;

    @Column(name = "option_id_advertised")
    private String optionIdAdvertised;

    @Column(name = "product_with_conversion_sales")
    private String productWithConversionSales;

    @Column(name = "option_id_with_conversion_sales")
    private String optionIdWithConversionSales;

    @Column(name = "ad_placement")
    private String adPlacement;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "impressions")
    private Long impressions;

    @Column(name = "clicks")
    private Long clicks;

    @Column(name = "ad_cost", precision = 15, scale = 2)
    private BigDecimal adCost;

    @Column(name = "click_through_rate", precision = 5, scale = 2)
    private BigDecimal clickThroughRate;

    @Column(name = "total_orders_1_day")
    private Long totalOrders1Day;

    @Column(name = "direct_orders_1_day")
    private Long directOrders1Day;

    @Column(name = "indirect_orders_1_day")
    private Long indirectOrders1Day;

    @Column(name = "total_sales_quantity_1_day")
    private Long totalSalesQuantity1Day;

    @Column(name = "direct_sales_quantity_1_day")
    private Long directSalesQuantity1Day;

    @Column(name = "indirect_sales_quantity_1_day")
    private Long indirectSalesQuantity1Day;

    @Column(name = "total_conversion_revenue_1_day", precision = 15, scale = 2)
    private BigDecimal totalConversionRevenue1Day;

    @Column(name = "direct_conversion_revenue_1_day", precision = 15, scale = 2)
    private BigDecimal directConversionRevenue1Day;

    @Column(name = "indirect_conversion_revenue_1_day", precision = 15, scale = 2)
    private BigDecimal indirectConversionRevenue1Day;

    @Column(name = "total_orders_14_days")
    private Long totalOrders14Days;

    @Column(name = "direct_orders_14_days")
    private Long directOrders14Days;

    @Column(name = "indirect_orders_14_days")
    private Long indirectOrders14Days;

    @Column(name = "total_sales_quantity_14_days")
    private Long totalSalesQuantity14Days;

    @Column(name = "direct_sales_quantity_14_days")
    private Long directSalesQuantity14Days;

    @Column(name = "indirect_sales_quantity_14_days")
    private Long indirectSalesQuantity14Days;

    @Column(name = "total_conversion_revenue_14_days", precision = 15, scale = 2)
    private BigDecimal totalConversionRevenue14Days;

    @Column(name = "direct_conversion_revenue_14_days", precision = 15, scale = 2)
    private BigDecimal directConversionRevenue14Days;

    @Column(name = "indirect_conversion_revenue_14_days", precision = 15, scale = 2)
    private BigDecimal indirectConversionRevenue14Days;

    @Column(name = "total_ad_roi_1_day", precision = 10, scale = 2)
    private BigDecimal totalAdRoi1Day;

    @Column(name = "direct_ad_roi_1_day", precision = 10, scale = 2)
    private BigDecimal directAdRoi1Day;

    @Column(name = "indirect_ad_roi_1_day", precision = 10, scale = 2)
    private BigDecimal indirectAdRoi1Day;

    @Column(name = "total_ad_roi_14_days", precision = 10, scale = 2)
    private BigDecimal totalAdRoi14Days;

    @Column(name = "direct_ad_roi_14_days", precision = 10, scale = 2)
    private BigDecimal directAdRoi14Days;

    @Column(name = "indirect_ad_roi_14_days", precision = 10, scale = 2)
    private BigDecimal indirectAdRoi14Days;

    @Column(name = "campaign_start_date")
    private String campaignStartDate;

}
